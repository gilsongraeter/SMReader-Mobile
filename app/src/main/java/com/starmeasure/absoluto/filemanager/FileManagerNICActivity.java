package com.starmeasure.absoluto.filemanager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.starmeasure.absoluto.Arquivo;
import com.starmeasure.absoluto.DeviceActivity;
import com.starmeasure.absoluto.R;
import com.starmeasure.absoluto.filemanager.adapter.FileManagerNICAdapter;

import org.apache.commons.net.ftp.FTPClient;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class FileManagerNICActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String classTag = "FM-NIC->";

    private Thread downloadThread;
    private FTPClient ftpClient;
    private boolean isDownloadRunning = false;
    private FileManagerNICAdapter adapter;
    private RecyclerView rvList;
    public static String resposta[] = { "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "" };
    public static String titulo = "";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_file_manager_nic);

        rvList = findViewById(R.id.afm_NIC_rv_list);

        findViewById(R.id.afm_NIC_imgbtn_close).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        lista();

        ftpClient = new FTPClient();
    }

    @Override
    public void onClick(@NotNull View v) {
        if (v.getId() == R.id.afm_NIC_imgbtn_close) {
            finish();
        }

    }

    @NonNull
    private ArrayList<File> getFileList() {
        ArrayList<File> mList = new ArrayList<>();
        File dir = new File(Arquivo.pathNIC());
        if (dir.exists()) {
            File[] list = dir.listFiles();
            if (list != null && list.length > 0) {
                for (File f : list) {
                        mList.add(f);
                }
            }
        } else {
            if (!dir.mkdir()) {
                Log.e(classTag, "Diretorio de configuração NIC não existe");
            }
        }

        return mList;
    }

    public void lista(){


        adapter = new FileManagerNICAdapter(this, getFileList());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(adapter);

        adapter.setOnItemClickListener((position, v) -> {
            File file = adapter.getFile(position);
            AlertDialog.Builder builde = new AlertDialog.Builder(this);
            builde.setTitle("Aviso");
            builde.setMessage("Usar ou deletar a predefinição:\n" + file.getName() + " ?\n\nDados excluídos não poderão ser recuperados.");
            builde.setNegativeButton("Usar", (dialog, which) -> {
                try {

                    titulo = file.getName();

                    DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                            .newDocumentBuilder();
                    Document dom = builder.parse(file);

                    dom.normalizeDocument();

                    Element root = dom.getDocumentElement();
                    System.out.println(root.getNodeName());

                    NodeList nList = dom.getElementsByTagName("Deteccao_falhas");

                    for (int temp = 0; temp < nList.getLength(); temp++)
                    {
                        Node node = nList.item(temp);
                        if (node.getNodeType() == Node.ELEMENT_NODE)
                        {
                            Element eElement = (Element) node;

                            resposta[0] = eElement.getAttribute("telefone1");
                            resposta[1] = eElement.getAttribute("telefone2");
                            resposta[2] = eElement.getAttribute("telefone3");
                            resposta[3] = eElement.getAttribute("telefone4");
                        }
                    }

                    nList = dom.getElementsByTagName("Eventos");

                    for (int temp = 0; temp < nList.getLength(); temp++)
                    {
                        Node node = nList.item(temp);
                        if (node.getNodeType() == Node.ELEMENT_NODE)
                        {
                            Element eElement = (Element) node;

                            resposta[4] = eElement.getAttribute("falta_energia");
                            resposta[5] = eElement.getAttribute("abertura_tampa");
                            resposta[6] = eElement.getAttribute("subtensao");
                            resposta[7] = eElement.getAttribute("sobretensao");
                        }
                    }

                    nList = dom.getElementsByTagName("Ciclos");

                    for (int temp = 0; temp < nList.getLength(); temp++)
                    {
                        Node node = nList.item(temp);
                        if (node.getNodeType() == Node.ELEMENT_NODE)
                        {
                            Element eElement = (Element) node;

                            resposta[8] = eElement.getAttribute("etapa");
                            resposta[9] = eElement.getAttribute("repeticoes");
                            resposta[10] = eElement.getAttribute("intervalo1");
                            resposta[11] = eElement.getAttribute("intervalo2");

                        }
                    }

                    nList = dom.getElementsByTagName("Keep_alive");

                    for (int temp = 0; temp < nList.getLength(); temp++)
                    {
                        Node node = nList.item(temp);
                        if (node.getNodeType() == Node.ELEMENT_NODE)
                        {
                            Element eElement = (Element) node;

                            resposta[12] = eElement.getAttribute("telefone");
                            resposta[13] = eElement.getAttribute("validade");
                            resposta[14] = eElement.getAttribute("frequencia");

                        }
                    }

                    nList = dom.getElementsByTagName("Modo_prateleira");

                    for (int temp = 0; temp < nList.getLength(); temp++)
                    {
                        Node node = nList.item(temp);
                        if (node.getNodeType() == Node.ELEMENT_NODE)
                        {
                            Element eElement = (Element) node;

                            resposta[15] = eElement.getAttribute("comando");

                        }
                    }

                    DeviceActivity.NIC = 1;

                    finish();




                } catch (Exception e) {
                    Toast.makeText(this, "Erro", Toast.LENGTH_SHORT);
                }


            }).setPositiveButton("Deletar", (dialog, which) -> {
                adapter.deleteFile(file, position);
            });
            builde.create();
            builde.show();
        });
        //array de retorno..... ou add todos em uma unica String para retornar e passar para o Device para finalizar.

    }


}
