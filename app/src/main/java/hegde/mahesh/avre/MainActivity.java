package hegde.mahesh.avre;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import hegde.mahesh.avre.adapters.HistoryAdapter;
import hegde.mahesh.avre.databinding.ActivityMainBinding;
import hegde.mahesh.avre.interpreter.BshInterpreter;
import hegde.mahesh.avre.interpreter.SnippetEvalException;
import hegde.mahesh.avre.interpreter.SnippetInterpreter;
import hegde.mahesh.avre.model.HistoryItem;

public class MainActivity extends AppCompatActivity {

    private int historyPos = -1;

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    List<HistoryItem> history = new ArrayList<>();
    HistoryAdapter adapter;
    private EditText codeEdit;
    private RecyclerView historyView;
    private NestedScrollView nsv;

    private String wasCurrentlyEditing;

    private SnippetInterpreter interpreter;
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final ByteArrayOutputStream err = new ByteArrayOutputStream();

    private void setCodeAndScroll(String code) {
        codeEdit.setText(code);
        nsv.post(() -> {
            codeEdit.requestFocus();
            nsv.fullScroll(View.FOCUS_DOWN);
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // initialize view references
        codeEdit = findViewById(R.id.text_code);
        nsv = findViewById(R.id.scroller_avre_prompt);
        historyView = findViewById(R.id.recycler_view_history);
        adapter = new HistoryAdapter(this,history);
        historyView.setAdapter(adapter);
        historyView.setLayoutManager(new LinearLayoutManager(this));

        // TODO: Should not really need this
        codeEdit.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> nsv.post(() -> {
            codeEdit.requestFocus();
            nsv.fullScroll(View.FOCUS_DOWN);
        }));

        // We want to display what interpreter writes to output on the app

        interpreter = new BshInterpreter();
        interpreter.setOutputStream(new PrintStream(out));
        interpreter.setErrorStream(new PrintStream(err));
        try {
            initializeInterpreter(interpreter);
        } catch (SnippetEvalException e) {
            e.printStackTrace();
        }
    }

    // set common variables etc...
    void initializeInterpreter(SnippetInterpreter interpreter) throws SnippetEvalException {
        interpreter.setVariable("context", this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void evalCode(View view) {
        String code = codeEdit.getText().toString();

        try {
            Object result = interpreter.eval(code);
            String valClass =  result != null ? (": " + result.getClass()) : "";

            // if any output to stdout, stderr, print it
            String outStr = readOutput(out);
            String errStr = readOutput(err);

            HistoryItem historyItem = HistoryItem.success(code, outStr, errStr,
                    result + valClass);
            history.add(historyItem);
            adapter.notifyItemInserted(history.size()-1);
        } catch (Exception e) {
            history.add(
                    HistoryItem.failure(code, readOutput(out), readOutput(err), e)
            );
            adapter.notifyItemInserted(history.size()-1);
        }
        historyPos = -1;
        setCodeAndScroll("");
    }

    // read output from a ByteArrayOutputStream and reset it
    private static String readOutput(ByteArrayOutputStream stream) {
        String result = stream.toString();
        stream.reset();
        return result;
    }

    public void clearCode(View view) {
        codeEdit.setText("");
    }

    public void prevSnippet(View view) {
        if (history.isEmpty()) {
            return;
        }
        if (historyPos == -1) {
            historyPos = history.size();
            wasCurrentlyEditing = codeEdit.getText().toString();
        }
        historyPos = Math.max(0, historyPos - 1);
        String codeText = history.get(historyPos).code;
        codeEdit.setText(codeText);
        codeEdit.setSelection(codeText.length());
    }

    public void nextSnippet(View view) {
        if (historyPos == -1) {
            return;
        }
        historyPos = Math.min(historyPos+1, history.size());
        if (historyPos == history.size()) {
            codeEdit.setText(wasCurrentlyEditing);
            codeEdit.setSelection(wasCurrentlyEditing.length());
            wasCurrentlyEditing = null;
            historyPos = -1;
        } else {
            String codeText = history.get(historyPos).code;
            codeEdit.setText(codeText);
            codeEdit.setSelection(codeText.length());
        }
    }

    public void clearAll(MenuItem item) {
        int count = history.size();
        history.clear();
        historyPos = -1;
        adapter.notifyItemRangeRemoved(0, count);
    }

    public void resetInterpreter(MenuItem item) {
        clearAll(item);
        interpreter.reset();
    }
}
