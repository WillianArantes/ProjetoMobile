package com.example.sgnaads;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText edNome, edEmail, edIDade, edDisciplina, edNotaPriBimestre, edNotaSegBimestre;
    private Button btEnviar, btLimpar;
    private TextView tvInformacoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Inicializando os campos com os IDs corretos
        edNome = findViewById(R.id.edNome);
        edEmail = findViewById(R.id.edEmail);
        edIDade = findViewById(R.id.edIdade);
        edDisciplina = findViewById(R.id.edDisciplina);
        edNotaPriBimestre = findViewById(R.id.edNotaPriBimestre); // Corrigido o ID
        edNotaSegBimestre = findViewById(R.id.edNotaSegBimestre);
        btEnviar = findViewById(R.id.btEnviar);
        btLimpar = findViewById(R.id.btLimpar);
        tvInformacoes = findViewById(R.id.tvInformacoes);

        btEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Pegando os valores dos campos e atribuindo as variaveis
                    String nome = edNome.getText().toString();
                    String email = edEmail.getText().toString();
                    String disciplina = edDisciplina.getText().toString();
                    String idade = edIDade.getText().toString();
                    String notaPriBimestre = edNotaPriBimestre.getText().toString();
                    String notaSegBimestre = edNotaSegBimestre.getText().toString();

                    //criando variaveis
                    String exibirInfo;
                    String msgResultado;
                    Double mediaNotas = 0.0;
                    Integer intIdade = 0;

                    //metodo para Validação de campos vazios
                    isEmpty(nome, email, disciplina, idade, notaPriBimestre, notaSegBimestre);
                    // Validação de e-mail usando regex
                    validacaoEmail(email);
                    // Validação de idade numérica

                    if (!isNumeric(idade)) {
                        tvInformacoes.setText("O valor digitado para idade não é numérico.");
                        return; // Interrompe a execução caso o valor não seja numérico
                    } else {
                        intIdade = Integer.parseInt(idade);
                    }
                    // Validações adicionais para idade
                    validacaoIdade(intIdade);
                    //metodo para validações para verificar notas entre 0 e 10.
                    validacaoNotas(notaPriBimestre, notaSegBimestre);
                    ///metodo para calcular media de notas.
                    mediaNotas = calcularMedia(notaPriBimestre,notaSegBimestre);
                    //metodo para validar a media do aluno.
                    msgResultado = validacaoMedia(mediaNotas);

                    //exibir as informações na tela
                    exibirInfo = "INFORMAÇÕES DO ALUNO"
                    + "\nNome: "+ nome
                    + "\nEmail: " + email
                    + "\nIdade: " + idade
                    + "\nDisciplina: " + disciplina
                    + "\nNotas: 1º e 2º bimestres: " + notaPriBimestre + "," + notaSegBimestre
                    + "\nMédia: " + mediaNotas
                    + "\n" + msgResultado;
                    tvInformacoes.setText(exibirInfo);

                } catch (NumberFormatException exNum) {
                    tvInformacoes.setText(exNum.getMessage());
                } catch (IllegalArgumentException exIllA) {
                    tvInformacoes.setText(exIllA.getMessage());
                } catch (NullPointerException exNull) {
                    tvInformacoes.setText(exNull.getMessage());
                } catch (Exception ex) {
                    tvInformacoes.setText(ex.getMessage());
                }
            }
        });
        btLimpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edNome.setText("");
                edEmail.setText("");
                edDisciplina.setText("");
                edIDade.setText("");
                edNotaPriBimestre.setText("");
                edNotaSegBimestre.setText("");
                tvInformacoes.setText("");
            }
        });
    }

    //metodos
    public void validacaoIdade(Integer idade){
        // Validações adicionais para idade
        Integer idadeMaxima = 70;
        Integer maxDigitos = 2;

        if (idade > idadeMaxima) {
            throw new NumberFormatException("Idade acima de " + idadeMaxima +
                    " não permitida.");
        } else if (idade < 0) {
            throw new NumberFormatException("A idade deve ser número positivo.");
        } else if (String.valueOf(idade).length() > maxDigitos) {
            throw new NumberFormatException("Quantidade de dígitos da" +
                    " idade acima do maximo permitido: " + maxDigitos + ".");
        }
    }
    public String validacaoMedia(Double mediaNotas){
        if(mediaNotas >= 70.0){
            return "Parabens, aluno aprovado alcançou a pontuação " +
                    "média " + mediaNotas + "!!!";
        }else{
            return  "Aluno reprovado, não alcançou a  pontuação média "
                    + mediaNotas + "!!!";
        }
    }
    public void validacaoEmail(String email){
        // Validação de e-mail usando regex
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!email.matches(regex)) {
            throw new IllegalArgumentException("Email inserido não é " +
                    "válido: formato incorreto.");
        }
    }
    public double calcularMedia(String nPriBimestre, String nSegBimestre){
        double nota1 = Double.parseDouble(nPriBimestre);
        double nota2 = Double.parseDouble(nSegBimestre);

        return (nota1 + nota2) / 2;
    }
    public void isEmpty(String nome, String email, String disciplina, String idade,
                            String nota1, String nota2) {
        // Verificação de campos vazios
        ArrayList<String[]> campos = new ArrayList<>();
        campos.add(new String[]{"Nome", nome});
        campos.add(new String[]{"Email", email});
        campos.add(new String[]{"Idade", idade});
        campos.add(new String[]{"Disciplina", disciplina});
        campos.add(new String[]{"Nota 1", nota1});
        campos.add(new String[]{"Nota 2", nota2});

        for (String[] campo : campos) {
            String nomeCampo = campo[0];
            String valorCampo = campo[1];

            if (TextUtils.isEmpty(valorCampo)) {
                throw new NullPointerException("Erro! O campo '" + nomeCampo + "' está vazio.");
            }
        }
    }

    public boolean isNumeric(String str) {
        // Tenta validar se a string pode ser convertida para um número inteiro ou decimal
        try {
            Integer.parseInt(str); // Verifica se a string pode ser convertida para um número
            return true; // Se conseguir converter, retorna true
        } catch (NumberFormatException e) {
            return false; // Se não conseguir, retorna false
        }
    }

    public void validacaoNotas(String notaPriBimestre, String notaSegBimestre){
        // Validação de notas numéricas
            double nota1 = Double.parseDouble(notaPriBimestre);
            Double nota2 = Double.parseDouble(notaSegBimestre);
            // Validação de notas numéricas
            if (!isNumeric(notaPriBimestre) || !isNumeric(notaSegBimestre)) {
                tvInformacoes.setText("As notas inseridas devem ser numéricas.");
                return;
            }
            if(nota1 < 0 || nota1 > 10 || nota2 < 0 || nota2 > 10) {
                tvInformacoes.setText("As notas devem estar entre o numero 0 e 10.");
            }
        }
}
