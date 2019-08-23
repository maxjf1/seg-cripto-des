package com.company;

public class Main {

    public static void main(String[] args) {
        // write your code here
        String cript = SDES.encrypt("ola mundo cruel cheio de xorumelos", 42);
        String decript = SDES.decrypt(cript, 42);
        System.out.println("Mensagem Criptografada:");
        System.out.println(cript);
        System.out.println("Mensagem Descriptografada:");
        System.out.println(decript);

    }
}
