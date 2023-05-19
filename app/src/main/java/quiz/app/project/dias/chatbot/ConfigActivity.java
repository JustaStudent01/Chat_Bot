package quiz.app.project.dias.chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import quiz.app.project.dias.chatbot.Database.AppDatabase;
import quiz.app.project.dias.chatbot.Database.User;
import quiz.app.project.dias.chatbot.Database.UserDao;
import quiz.app.project.dias.chatbot.Log_Reg_Activities.LoginActivity;

public class ConfigActivity extends AppCompatActivity {
    private Button btnLimpar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.btnLimpar = findViewById(R.id.btnLimpar);

        btnLimpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Database code
                AppDatabase db = Room.databaseBuilder(ConfigActivity.this, AppDatabase.class, "AppDatabase").build();
                UserDao userDao = db.getUserDao();

                new AlertDialog.Builder(ConfigActivity.this)
                        .setTitle("Deseja limpar os dados da aplicação?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ExecutorService executor = Executors.newSingleThreadExecutor();
                                executor.execute(() -> {
                                    userDao.deleteAll();
                                    List userList = userDao.getAllUsers();

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (userList != null) {
                                                Toast.makeText(ConfigActivity.this, "Dados limpos com sucesso!", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(ConfigActivity.this,MainScreenActivity.class);
                                                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(ConfigActivity.this).toBundle();
                                                ConfigActivity.this.startActivity(intent, bundle);
                                                finish();
                                            } else {
                                                Toast.makeText(ConfigActivity.this, "Não foi possível limpar os dados!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                    executor.shutdown();
                                });
                            }
                        })
                        .setNegativeButton("Não", null)
                        .show();
            }
        });
    }
}