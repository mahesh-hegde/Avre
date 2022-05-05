package hegde.mahesh.avre;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bsh.EvalError;
import bsh.ParseException;
import hegde.mahesh.avre.databinding.ActivityMainBinding;
import bsh.Interpreter;
import hegde.mahesh.avre.model.HistoryItem;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    List<HistoryItem> history = new ArrayList<>();

    private Interpreter interpreter;

    private Integer initialTextColor;

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final ByteArrayOutputStream err = new ByteArrayOutputStream();

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
        EditText editCode = findViewById(R.id.text_code);
        TextView resultText = findViewById(R.id.text_result);
        String code = editCode.getText().toString();
        if (initialTextColor == null) {
            initialTextColor = resultText.getCurrentTextColor();
        }
        try {
            Object result = interpreter.eval(code);
            String valClass = result != null ? result.getClass().toString() : "(Null)";

            // if any output to stdout, stderr, print it
            String outStr = readOutput(out);
            String errStr = readOutput(err);
            resultText.setText(
                    String.format("%s%s%s : %s\n", outStr, errStr, result, valClass)
            );
            resultText.setTextColor(initialTextColor);

            HistoryItem historyItem = HistoryItem.success(code, outStr, errStr, Objects.toString(result));
            history.add(historyItem);
        } catch (Exception e) {
            Throwable cause;
            String message = e.getMessage();

            // Clean up error message for some errors
            if (e instanceof EvalError) {
                cause = e.getCause();
                if (cause != null) {
                    message = cause.getMessage();
                }
            }
            resultText.setText(message);
            resultText.setTextColor(0xffff0000);
        }
    }

    // read output from a ByteArrayOutputStream and reset it
    // append \n for readability, if there's any output.
    private static String readOutput(ByteArrayOutputStream stream) {
        String result = stream.toString();
        if (!result.isEmpty())
            result += "\n";
        stream.reset();
        return result;
    }
}