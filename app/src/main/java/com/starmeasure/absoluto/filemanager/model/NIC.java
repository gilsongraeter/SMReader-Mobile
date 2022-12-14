package com.starmeasure.absoluto.filemanager.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

public class NIC {

    public static final String extensao = "xml";
    private static final String mTag = "ThaNIC";

    private String nome;
    private String modelo;
    private String iu;
    private int versao;
    private int revisao;
    private File arquivo;
    private short maxSize;
    private byte[] dadosBrutos;
    private ArrayList<byte[]> dataToSend;

    public NIC(@NonNull String nome, @NonNull String modelo, @NonNull String iu, int versao, int revisao, @Nullable File arquivo) {
        this.nome = nome;
        this.modelo = modelo;
        this.iu = iu;
        this.versao = versao;
        this.revisao = revisao;
        this.arquivo = arquivo;
    }

    @NonNull
    public static String getExtensao() {
        return extensao;
    }

    @NonNull
    public String getNome() {
        return nome;
    }

    @NonNull
    public String getModelo() {
        return modelo;
    }

    @NonNull
    public String getIu() {
        return iu;
    }

    public int getVersao() {
        return versao;
    }

    public int getRevisao() {
        return revisao;
    }

    @Nullable
    public File getArquivo() {
        return arquivo;
    }

    public byte[] getDadosBrutos() {
        return dadosBrutos;
    }

    public void setDadosBrutos(byte[] dadosBrutos) {
        this.dadosBrutos = dadosBrutos;
    }

    public short getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(short maxSize) {
        this.maxSize = maxSize;
    }

    public ArrayList<byte[]> getDataToSend() {
        return dataToSend;
    }

    public void setDataToSend(ArrayList<byte[]> dataToSend) {
        this.dataToSend = dataToSend;
    }

    @NotNull
    @Override
    public String toString() {
        return "NIC {" +
                "\nNome: " + nome +
                "\nModelo Multiponto: " + modelo +
                "\nIU:" + iu +
                "\nVersao: " + versao +
                "\nRevisao: " + revisao +
                "\nArquivo: " + arquivo +
                "\n}";
    }
}
