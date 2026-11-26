// # 1. Entrar na pasta onde estão os arquivos .java do Selection Sort:
// cd ...\SelectionSort\selection sort
// # 2. Compilar todos os arquivos Java:
// javac -encoding UTF-8 -d target/classes *.java
// # 3. Executar a suíte de testes com todas as 24 asserções:
// java "-Dfile.encoding=UTF-8" -cp target/classes TestesSelectionSort

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Testes Selection Sort.
 * <p>
 * Valida unidade, integridade das matrículas, ordenação, estabilidade/comportamento com empates,
 * multiconjunto (nenhum registro perdido), casos de borda (vazio, unitário, invertido),
 * leitura resiliente de arquivo e homologação na base de 200.000 alunos.
 * </p>
 *
 * @author Gabriel Lira Figueira
 * @author Eduarda Santana Santos
 * @author João Luís Aragão Rocha
 * @version 1.0
 */
public class TestesSelectionSort {

    /** Contador total de verificações executadas na suíte. */
    public static int totalTestes = 0;

    /** Contador de verificações concluídas com sucesso. */
    public static int sucessos = 0;

    /** Contador de falhas detectadas. */
    public static int falhas = 0;

    /**
     * Valida uma asserção e exibe visualmente no console o resultado com indicador formatado.
     *
     * @param condicao  resultado booleano da verificação
     * @param descricao texto descritivo do que está sendo testado
     */
    public static void verificar(boolean condicao, String descricao) {
        totalTestes++;
        if (condicao) {
            sucessos++;
            System.out.printf("  [OK]    %s%n", descricao);
        } else {
            falhas++;
            System.out.printf("  [FALHA] %s%n", descricao);
        }
    }

    /**
     * Imprime um cabeçalho estilizado de início para o relatório no terminal.
     *
     * @param titulo texto do título principal
     */
    public static void imprimirCabecalho(String titulo) {
        System.out.println("\n================================================================================");
        System.out.println("  " + titulo);
        System.out.println("================================================================================");
    }

    /**
     * Imprime um divisor de seção para separar os cenários de teste.
     *
     * @param titulo identificação do cenário de teste
     */
    public static void imprimirSecao(String titulo) {
        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.println("  " + titulo);
        System.out.println("--------------------------------------------------------------------------------");
    }

    /**
     * Verifica se o vetor de alunos está estritamente em ordem crescente por matrícula.
     *
     * @param alunos vetor de alunos
     * @return true se estiver ordenado de forma crescente, false caso contrário
     */
    public static boolean estaOrdenadoCrescente(Aluno[] alunos) {
        for (int i = 0; i < alunos.length - 1; i++) {
            if (alunos[i].getMatricula() > alunos[i + 1].getMatricula()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Mapeia a frequência de cada linha de aluno para validação de multiconjunto.
     *
     * @param alunos vetor de alunos
     * @return mapa com chave como linhaOriginal e valor como frequência
     */
    public static Map<String, Integer> contarOcorrencias(Aluno[] alunos) {
        Map<String, Integer> mapa = new HashMap<>();
        for (Aluno a : alunos) {
            String chave = a.getLinhaOriginal();
            mapa.put(chave, mapa.getOrDefault(chave, 0) + 1);
        }
        return mapa;
    }

    /**
     * Ponto de entrada da suíte de testes do Selection Sort.
     *
     * @param args argumentos de linha de comando
     * @throws IOException se houver erro ao manipular arquivos nos testes
     */
    public static void main(String[] args) throws IOException {
        imprimirCabecalho("SUÍTE DE TESTES VISUAIS E AUTOMATIZADOS: UEDT (SELECTION SORT)");

        // -------------------------------------------------------------------------
        // [Cenário 1] Precisão Numérica da Matrícula de 14 Dígitos (Long vs Int)
        // -------------------------------------------------------------------------
        imprimirSecao("CENÁRIO 1: Matrícula de 14 Dígitos (Precisão Numérica de 64 bits - Long)");
        System.out.println("  Validando que números de 14 dígitos (ex: 10000000512526) não sofrem overflow numérico:");
        
        long matExemplo = 10000000512526L;
        Aluno alunoTeste = new Aluno(matExemplo, "10000000512526 | 26/02/2021 | Yan Ferreira | 09/10/1992 | Administração");
        
        verificar(alunoTeste.getMatricula() == 10000000512526L, "Matrícula armazenada exatamente como long positivo sem truncamento");
        verificar(alunoTeste.getMatricula() > Integer.MAX_VALUE, "Matrícula é superior a Integer.MAX_VALUE (2.147.483.647), exigindo tipo long");
        verificar(alunoTeste.getLinhaOriginal().contains("Yan Ferreira"), "Linha original mantida integralmente para escrita fiel");

        // -------------------------------------------------------------------------
        // [Cenário 2] Casos de Borda (Corner Cases): Vazio, Unitário e Dois Elementos
        // -------------------------------------------------------------------------
        imprimirSecao("CENÁRIO 2: Casos de Borda (Corner Cases) e Tamanhos Mínimos");
        
        Aluno[] vazio = new Aluno[0];
        SelectionSort.selectionSort(vazio);
        verificar(vazio.length == 0, "Vetor vazio: algoritmo não quebra e mantém tamanho zero");

        Aluno[] unitario = new Aluno[]{ alunoTeste };
        SelectionSort.selectionSort(unitario);
        verificar(unitario.length == 1 && unitario[0].getMatricula() == matExemplo, "Vetor unitário: elemento permanece intacto e posicionado");

        Aluno aMenor = new Aluno(10000000000001L, "10000000000001 | 01/01/2020 | Aluno A | 01/01/2000 | Curso A");
        Aluno aMaior = new Aluno(10000000000002L, "10000000000002 | 01/01/2020 | Aluno B | 01/01/2000 | Curso B");
        
        Aluno[] doisInvertidos = new Aluno[]{ aMaior, aMenor };
        SelectionSort.selectionSort(doisInvertidos);
        verificar(doisInvertidos[0].getMatricula() == 10000000000001L && doisInvertidos[1].getMatricula() == 10000000000002L,
                "Dois elementos invertidos: troca simples realizada com sucesso");

        // -------------------------------------------------------------------------
        // [Cenário 3] Amostra Controlada e Ordenação Visual (Crescente)
        // -------------------------------------------------------------------------
        imprimirSecao("CENÁRIO 3: Ordenação Crescente Visual em Amostra Reduzida");
        
        Aluno a1 = new Aluno(10000000584020L, "10000000584020 | José Pedro Sales");
        Aluno a2 = new Aluno(10000000512526L, "10000000512526 | Yan Ferreira");
        Aluno a3 = new Aluno(10000000988105L, "10000000988105 | Maria Alice Pires");
        Aluno a4 = new Aluno(10000000100000L, "10000000100000 | Carlos Eduardo");

        Aluno[] amostra = new Aluno[]{ a1, a2, a3, a4 };
        
        System.out.println("  [Vetor Desordenado]:");
        for (Aluno a : amostra) System.out.println("    " + a.getLinhaOriginal());

        SelectionSort.selectionSort(amostra);

        System.out.println("\n  [Vetor Ordenado pelo Selection Sort (Crescente)]:");
        for (Aluno a : amostra) System.out.println("    " + a.getLinhaOriginal());

        verificar(estaOrdenadoCrescente(amostra), "Amostra ordenada estritamente em ordem crescente de matrícula");
        verificar(amostra[0].getMatricula() == 10000000100000L, "Menor matrícula posicionada no topo (#1)");
        verificar(amostra[3].getMatricula() == 10000000988105L, "Maior matrícula posicionada na base (#4)");

        // -------------------------------------------------------------------------
        // [Cenário 4] Melhor Caso vs Pior Caso (2.000 Registros)
        // -------------------------------------------------------------------------
        imprimirSecao("CENÁRIO 4: Análise de Desempenho - Melhor Caso vs Pior Caso (2.000 itens)");
        
        int nCarga = 2000;
        Aluno[] melhorCaso = new Aluno[nCarga];
        for (int i = 0; i < nCarga; i++) {
            melhorCaso[i] = new Aluno(10000000000000L + i, "Aluno " + i);
        }

        Aluno[] piorCaso = new Aluno[nCarga];
        for (int i = 0; i < nCarga; i++) {
            piorCaso[i] = melhorCaso[nCarga - 1 - i];
        }

        long t0Melhor = System.currentTimeMillis();
        SelectionSort.selectionSort(melhorCaso);
        long tMelhor = System.currentTimeMillis() - t0Melhor;

        long t0Pior = System.currentTimeMillis();
        SelectionSort.selectionSort(piorCaso);
        long tPior = System.currentTimeMillis() - t0Pior;

        System.out.printf("  -> Melhor Caso (já ordenado) : %d ms (executa N(N-1)/2 comparações)%n", tMelhor);
        System.out.printf("  -> Pior Caso   (ordem inversa): %d ms (executa N(N-1)/2 comparações)%n", tPior);
        
        verificar(estaOrdenadoCrescente(melhorCaso), "Melhor caso permanece ordenado de forma estável");
        verificar(estaOrdenadoCrescente(piorCaso), "Pior caso invertido para ordem crescente com sucesso");

        // -------------------------------------------------------------------------
        // [Cenário 5] Leitura Resiliente de Arquivo (lerArquivo)
        // -------------------------------------------------------------------------
        imprimirSecao("CENÁRIO 5: Leitura Resiliente (Linhas Inválidas, Espaços e Cabeçalhos)");
        
        Path tempEntrada = Files.createTempFile("teste_leitura_uedt_", ".txt");
        try {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(tempEntrada.toFile()))) {
                bw.write("10000000000001 | 01/01/2021 | Aluno Valido 1 | 01/01/2000 | Computação");
                bw.newLine();
                bw.write(""); // Linha vazia
                bw.newLine();
                bw.write("   "); // Linha com espaços
                bw.newLine();
                bw.write("Matricula Invalida Nao Numerica | 01/01/2021 | Aluno Erro | 01/01/2000 | Direito");
                bw.newLine();
                bw.write("10000000000002 | 02/02/2021 | Aluno Valido 2 | 02/02/2000 | Medicina");
                bw.newLine();
            }

            Aluno[] lidos = SelectionSort.lerArquivo(tempEntrada.toString());
            verificar(lidos.length == 2, "Leitura ignorou linhas em branco e matrícula não numérica (esperado: 2 registros)");
            verificar(lidos[0].getMatricula() == 10000000000001L && lidos[1].getMatricula() == 10000000000002L,
                    "Registros válidos extraídos com exatidão da entrada mista");
        } finally {
            Files.deleteIfExists(tempEntrada);
        }

        // -------------------------------------------------------------------------
        // [Cenário 6] Persistência Física em Disco (salvarArquivo + Releitura)
        // -------------------------------------------------------------------------
        imprimirSecao("CENÁRIO 6: Persistência Física em Disco e Fidelidade das Linhas");
        
        Path tempSaida = Files.createTempFile("teste_saida_uedt_", ".txt");
        try {
            Aluno[] dadosGravar = new Aluno[]{ a2, a1 };
            SelectionSort.selectionSort(dadosGravar);
            SelectionSort.salvarArquivo(tempSaida.toString(), dadosGravar);

            Aluno[] relidos = SelectionSort.lerArquivo(tempSaida.toString());
            verificar(relidos.length == 2, "Arquivo gravado contém exatamente 2 registros");
            verificar(estaOrdenadoCrescente(relidos), "Releitura do arquivo salvo comprova ordenação física no disco");
            verificar(relidos[0].getLinhaOriginal().equals(a2.getLinhaOriginal()),
                    "Linha original gravada caractere por caractere sem corrupção");
        } finally {
            Files.deleteIfExists(tempSaida);
        }

        // -------------------------------------------------------------------------
        // [Cenário 7] Integridade de Multiconjunto em Massa Aleatória (1.000 itens)
        // -------------------------------------------------------------------------
        imprimirSecao("CENÁRIO 7: Integridade de Multiconjunto (Sem Perda ou Duplicação)");
        
        int nAleat = 1000;
        Random rng = new Random(12345);
        Aluno[] aleatorios = new Aluno[nAleat];
        for (int i = 0; i < nAleat; i++) {
            long mat = 10000000000000L + rng.nextInt(50000);
            aleatorios[i] = new Aluno(mat, mat + " | 01/01/2021 | Aluno Random " + i + " | 01/01/2000 | Curso " + (i % 5));
        }

        Map<String, Integer> antes = contarOcorrencias(aleatorios);
        SelectionSort.selectionSort(aleatorios);
        Map<String, Integer> depois = contarOcorrencias(aleatorios);

        verificar(aleatorios.length == nAleat, "Quantidade total preservada após ordenação (1.000 registros)");
        verificar(estaOrdenadoCrescente(aleatorios), "Vetor aleatório completamente ordenado pelo Selection Sort");
        verificar(antes.equals(depois), "Integridade matemática de multiconjunto: nenhuma linha foi perdida, alterada ou duplicada");

        // -------------------------------------------------------------------------
        // [Cenário 8] Homologação da Base Oficial (alunos_ordenados.txt - 200.000 Alunos)
        // -------------------------------------------------------------------------
        imprimirSecao("CENÁRIO 8: Homologação na Base Oficial Processada (alunos_ordenados.txt)");
        
        File arqOficialOrdenado = new File("alunos_ordenados.txt");
        verificar(arqOficialOrdenado.exists(), "Arquivo oficial 'alunos_ordenados.txt' existe no diretório");

        if (arqOficialOrdenado.exists()) {
            System.out.println("  Lendo e validando os 200.000 registros da base oficial ordenada...");
            long t0Leitura = System.currentTimeMillis();
            Aluno[] oficial = SelectionSort.lerArquivo(arqOficialOrdenado.getPath());
            long tLeitura = System.currentTimeMillis() - t0Leitura;

            verificar(oficial.length == 200000, "Base oficial contém exatamente 200.000 registros de alunos válidos");
            verificar(estaOrdenadoCrescente(oficial), "Homologação: 200.000 alunos 100% ordenados em ordem crescente de matrícula");
            verificar(oficial[0].getMatricula() == 10000000000000L, "Primeira matrícula (Menor): 10000000000000 (Isaac Sá - Medicina)");
            verificar(oficial[199999].getMatricula() == 10000000199999L, "Última matrícula (Maior): 10000000199999 (Diogo Rocha - Administração)");

            imprimirResumoFinal(oficial.length, tLeitura, oficial);
        } else {
            imprimirResumoFinal(0, 0, null);
        }
    }

    /**
     * Imprime o quadro resumo final com a contagem total de asserções e diagnóstico visual.
     */
    public static void imprimirResumoFinal(int totalOficial, long tLeitura, Aluno[] oficial) {
        System.out.println("\n================================================================================");
        System.out.println("  RESUMO FINAL DOS TESTES EXECUTADOS");
        System.out.println("================================================================================");
        System.out.printf("  Total de asserções executadas : %d%n", totalTestes);
        System.out.printf("  Verificações com sucesso      : %d%n", sucessos);
        System.out.printf("  Falhas detectadas             : %d%n", falhas);
        if (falhas == 0) {
            System.out.println("  STATUS GERAL                  : >>> TODOS OS TESTES PASSARAM COM 100% DE SUCESSO! <<<");
        } else {
            System.out.println("  STATUS GERAL                  : >>> ATENÇÃO: EXISTEM FALHAS NOS TESTES! <<<");
        }
        System.out.println("================================================================================");

        if (oficial != null && oficial.length > 0) {
            System.out.println("\n--------------------------------------------------------------------------------");
            System.out.println("  DIAGNÓSTICO VISUAL DE PRODUÇÃO (BASE OFICIAL UEDT - 200.000 ALUNOS)");
            System.out.println("--------------------------------------------------------------------------------");
            System.out.printf("  Total de registros homologados : %,d alunos matriculados%n", totalOficial);
            System.out.printf("  Tempo de validação e leitura   : %d ms%n", tLeitura);
            System.out.println("\n  [Extremidades da Base Oficial Ordenada]:");
            System.out.printf("  #1 (Menor Matrícula)           : %s%n", oficial[0].getLinhaOriginal());
            System.out.printf("  #2                             : %s%n", oficial[1].getLinhaOriginal());
            System.out.printf("  #3                             : %s%n", oficial[2].getLinhaOriginal());
            System.out.println("  ...");
            System.out.printf("  #200.000 (Maior Matrícula)     : %s%n", oficial[oficial.length - 1].getLinhaOriginal());
            System.out.println("--------------------------------------------------------------------------------\n");
        }
    }
}
