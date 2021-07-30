package com.pierre.dianamq;

import static java.nio.charset.StandardCharsets.UTF_8;

import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.MqttGlobalPublishFilter;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;

public class home extends AppCompatActivity {

    final String host = "82ae9dd3e5474224a2f9a928bd07cb45.s1.eu.hivemq.cloud";
    final String username = "pierre kasongo";
    final String password = "Iamhappy82844";

    String topicNiveau ="pierre/station_meteo/niveau/";
    String topicPompe_in = "pierre/station_meteo/pompe/entre/";
    String topicPompe_out = "pierre/station_meteo/pompe/sortie/";
    String topicDebit_in = "pierre/station_meteo/debit/entre/";
    String topicDebit_out = "pierre/station_meteo/debit/sortie/";
    String topicVanne_in = "pierre/station_meteo/vanne/entre/";
    String topicVanne_out = "pierre/station_meteo/vanne/sortie/";
    String topicEtat = "pierre/station_meteo/etat/";
    String topicUrgence = "pierre/station_meteo/urgence/";

    SwitchCompat btn_pompe1, btn_pompe2, btn_vanne1, btn_vanne2;
    TextView debit_in, debit_out, texte_niv;
    ProgressBar niv_citerne;
    ImageButton btn_arret;

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btn_pompe1 = findViewById(R.id.btn_pompe1);
        btn_pompe2 = findViewById(R.id.btn_pompe2);

        niv_citerne = findViewById(R.id.niveauCiterne);

        debit_in = findViewById(R.id.debit_in);
        debit_out = findViewById(R.id.debit_out);

        btn_vanne1 = findViewById(R.id.btn_vanne1);
        btn_vanne2 = findViewById(R.id.btn_vanne2);

        btn_arret = findViewById(R.id.btn_arret);
        texte_niv = findViewById(R.id.texte_niv_citerne);

        final Mqtt5AsyncClient client = (Mqtt5AsyncClient) MqttClient.builder()
                .useMqttVersion5()
                .serverHost(host)
                .serverPort(8883)
                .sslWithDefaultConfig()
                .automaticReconnectWithDefaultConfig()
                .buildAsync();

        client.connectWith()
                .simpleAuth()
                .username(username)
                .password(UTF_8.encode(password))
                .applySimpleAuth()
                .cleanStart(true)
                .send();

        client.subscribeWith()
                .topicFilter(topicNiveau)
                .callback(mqtt5Publish -> {
                   /*
                   le problème réside à ce niveau
                   */
                    texte_niv.setText(new String(String.valueOf(UTF_8.decode(mqtt5Publish.getPayload().get()))));
                })
                .send();

        client.subscribeWith()
                .topicFilter(topicDebit_in)
                .callback(mqtt5Publish -> {
                    //configuration du textview pour le debit
                })
                .send();

        client.subscribeWith()
                .topicFilter(topicDebit_out)
                .callback(mqtt5Publish -> {
                   //configuration du textvieew pour le débit sortant
                })
                .send();


        btn_pompe1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn_pompe1.isChecked()){
                    client.publishWith()
                            .topic(topicPompe_in)
                            .payload("allumer_pompe1".getBytes())
                            .responseTopic(topicEtat)
                            .send();

                } else {
                    client.publishWith()
                            .topic(topicPompe_in)
                            .payload("eteindre_pompe1".getBytes())
                            .responseTopic(topicEtat)
                            .send();

                }
            }
        });

        btn_pompe2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn_pompe2.isChecked()){
                    client.publishWith()
                            .topic(topicPompe_out)
                            .payload("allumer_pompe2".getBytes())
                            .responseTopic(topicEtat)
                            .send();

                } else {
                    client.publishWith()
                            .topic(topicPompe_out)
                            .payload("eteindre_pompe2".getBytes())
                            .responseTopic(topicEtat)
                            .send();
                }
            }
        });

        btn_vanne1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn_vanne1.isChecked()){
                    client.publishWith()
                            .topic(topicVanne_in)
                            .payload("ouvrir_vanne1".getBytes())
                            .responseTopic(topicEtat)
                            .send();

                } else {
                    client.publishWith()
                            .topic(topicPompe_in)
                            .payload("fermer_vanne1".getBytes())
                            .responseTopic(topicEtat)
                            .send();
                }
            }
        });

        btn_vanne2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn_vanne2.isChecked()){
                    client.publishWith()
                            .topic(topicVanne_out)
                            .payload("ouvrir_vanne2".getBytes())
                            .responseTopic(topicEtat)
                            .send();

                } else {
                    client.publishWith()
                            .topic(topicPompe_out)
                            .payload("fermer_vanne2".getBytes())
                            .responseTopic(topicEtat)
                            .send();
                }
            }
        });

        btn_arret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                client.publishWith()
                        .topic(topicUrgence)
                        .payload("tout_arreter".getBytes())
                        .responseTopic(topicEtat)
                        .send();
            }
        });

    }
}