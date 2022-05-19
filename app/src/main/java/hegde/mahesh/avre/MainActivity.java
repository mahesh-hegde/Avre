package hegde.mahesh.avre;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.core.widget.NestedScrollView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bsh.EvalError;
import bsh.ParseException;
import hegde.mahesh.avre.adapters.HistoryAdapter;
import hegde.mahesh.avre.databinding.ActivityMainBinding;
import bsh.Interpreter;
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

    private Interpreter interpreter;
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

        // We want to display what interpreter writes to output on the app

        System.setOut(new PrintStream(out));
        System.setErr(new PrintStream(err));
        interpreter = new Interpreter();
        try {
            initializeInterpreter(interpreter);
        } catch (EvalError evalError) {
            evalError.printStackTrace();
        }
    }

    // set common variables etc...
    void initializeInterpreter(Interpreter interpreter) throws EvalError {
        interpreter.set("context", this);
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
            String valClass =  result != null ? (": " + result.getClass().toString()) : "";

            // if any output to stdout, stderr, print it
            String outStr = readOutput(out);
            String errStr = readOutput(err);

            HistoryItem historyItem = HistoryItem.success(code, outStr, errStr,
                    Objects.toString(result) + valClass);
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
    // append \n for readability, if there's any output.
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
}
