package com.epifi.elektu;

public class PreguntasQuizOnline {

    public String mPreguntas[] = {
            "¿Cuál de los siguientes cuentos no fue escrito por Charles Perrault?",
            "¿A qué período pertenece Góngora?",
            "¿Quién escribió 'Es mejor ser rey de tu silecio que esclavo de tus palabras'?",





    };

    private String mChoices[] [] = {
            {"El Gato con Botas","Barba azul","Pulgarcito","El Sastrecillo Valiente"},
            {"Generación del 98","Siglo de Oro","Generación del 27","Siglo de las luces"},
            {"Shakespeare","Cicerón","Aristóteles","Nietzsche"}

    };

    private String mCorrectAnswer [] = {"El Sastrecillo Valiente","Siglo de Oro","Shakespeare"};

    public String getPregunta(int a){
        String pregunta = mPreguntas[a];
        return pregunta;
    }
    public String getChoice1(int a){
        String choice  = mChoices[a][0];
        return choice;
    }
    public String getChoice2(int a){
        String choice  = mChoices[a][1];
        return choice;
    }
    public String getChoice3(int a){
        String choice  = mChoices[a][2];
        return choice;
    }
    public String getChoice4(int a){
        String choice  = mChoices[a][3];
        return choice;
    }
    public String getCorrectAnswer (int a){
        String answer = mCorrectAnswer[a];
        return answer;

    }
}
