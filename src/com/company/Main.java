package com.company;

public class Main {

    public static void main(String[] args) {
        // write your code here
        int chave = 24;
        String cript = SDES.encrypt("Estou seguro!", chave);
        String decript = SDES.decrypt(cript, chave);
        System.out.println("Mensagem Criptografada:");
        System.out.println(cript);
        System.out.println("Mensagem Descriptografada:");
        System.out.println(decript);
    }
}
