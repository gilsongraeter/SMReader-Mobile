package com.starmeasure.absoluto.filemanager.controller;

import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;

import com.starmeasure.absoluto.filemanager.model.NIC;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class NICController {

    @Nullable
    public static NIC build(@NotNull String fileName) {
        String[] nameList = fileName.split("_");

        String nome = nameList[0];
        String modeloMultiponto = nameList[1];
        String iu = nameList[2];
        String[] versaocomposta = nameList[3].split("\\.");
        int versao = Integer.parseInt(versaocomposta[0]);
        int revisao = Integer.parseInt(versaocomposta[1]);
        String extensao = versaocomposta[2];
        if (extensao.equals(NIC.extensao) || extensao.equals("zip")) {
            return new NIC(nome, modeloMultiponto, iu, versao, revisao, null);
        } else {
            return null;
        }
    }

    @Nullable
    public static NIC build(@NotNull String fileName, @NotNull File file) {
        String[] nameList = fileName.split("_");

        String nome = nameList[0];
        String modeloMultiponto = nameList[1];
        String iu = nameList[2];
        String[] versaocomposta = nameList[3].split("\\.");
        int versao = Integer.parseInt(versaocomposta[0]);
        int revisao = Integer.parseInt(versaocomposta[1]);
        String extensao = versaocomposta[2];
        if (extensao.equals(NIC.extensao) || extensao.equals("zip")) {
            return new NIC(nome, modeloMultiponto, iu, versao, revisao, file);
        } else {
            return null;
        }
    }

    public static boolean testaVersaoERevisaoIgual(@NotNull String fileName, @NotNull String[] versaoComposta) {
        boolean resultado = false;
        int versao = Integer.parseInt(versaoComposta[0]);
        int revisao = Integer.parseInt(versaoComposta[1]);
        NIC NIC = build(fileName);
        Log.i("tutsu", "Versão medidor: "+versao+" Revisão medidor: "+revisao);
        if (NIC != null) {
            Log.i("tutsu", NIC.toString());
            resultado = NIC.getVersao() == versao && NIC.getRevisao() == revisao;
            return resultado;
        } else {
            return true;
        }
    }

    /**
     * @return verdadeiro se a versão do arquivo é superior a versão do medidor
     */
    public static boolean testaVersaoERevisaoDiferente(@NotNull String fileName, @NotNull String[] versaoComposta) {
        int versao = Integer.parseInt(versaoComposta[0]);
        int revisao = Integer.parseInt(versaoComposta[1]);
        NIC NIC = build(fileName);
        boolean result = false;
        if (NIC != null) {
            if (NIC.getVersao() == versao) {
                if ((NIC.getRevisao() > revisao)||(versao > 80)) {
                    result = true;
                }
            } else if ((NIC.getVersao() > versao)||(versao > 80)){
                result = true;
            }
        }

        return result;
    }

    @Nullable
    public static byte[] getBytesNICDePrograma(NIC NIC) {
        final String mTag = "BuildBytes";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.i(mTag, "Montando Configuração de NIC - Android Oreo ou Superior");
            try {
                if (NIC.getArquivo() != null)
                return Files.readAllBytes(NIC.getArquivo().toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                if (NIC.getArquivo() != null) {
                    byte[] mList = new byte[(int) NIC.getArquivo().length()];
                    BufferedInputStream buf = new BufferedInputStream(new FileInputStream(NIC.getArquivo()));
                    int bytesRead = buf.read(mList, 0, mList.length);
                    if (bytesRead == mList.length) {
                        buf.close();
                        return mList;
                    } else {
                        buf.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static ArrayList<byte[]> dividePacks(NIC NIC) {
        ArrayList<byte[]> mList = new ArrayList<>();
        if (NIC.getDadosBrutos() != null) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            for (int i=0; i < NIC.getDadosBrutos().length; i++) {
                byte b = NIC.getDadosBrutos()[i];
                if (i < 15) {
                    os.write(b);
                } else if (i == 15) {
                    mList.add(os.toByteArray());
                    os.reset();
                    os.write(b);
                } else {
                    os.write(b);
                    if (os.toByteArray().length == NIC.getMaxSize()) {
                        mList.add(os.toByteArray());
                        os.reset();
                    } else if ((os.toByteArray().length <= NIC.getMaxSize()) && i == (NIC.getDadosBrutos().length-1)) {
                        mList.add(os.toByteArray());
                    }
                }
            }
        }
        return mList;
    }

    @Nullable
    public static byte[] getByteToFrom(byte[] list, int start, int maxSize) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (int i = start; i < list.length; i++) {
            byte mByte = list[i];
            outputStream.write(mByte);
            if (i == (maxSize-1)) {
                return outputStream.toByteArray();
            } else if (i == (list.length-1)) {
                return outputStream.toByteArray();
            }
        }
        return null;
    }
}
