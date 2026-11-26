import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Lê um arquivo de alunos, ordena pela matrícula em ordem crescente
 * usando Selection Sort e grava o resultado em outro arquivo.
 *
 * Uso: java SelectionSort [arquivoEntrada] [arquivoSaida]
 * Padrão: UEDT_SelectionSort.txt -> alunos_ordenados.txt
 *
 * @author Eduarda Santana Santos
 * @author Gabriel Lira Figueira
 * @author João Luís Aragão Rocha
 */
public class SelectionSort {

    /**
     * Executa a leitura, ordenação e gravação dos dados dos alunos.
     *
     * @param args argumentos da linha de comando: arquivo de entrada e arquivo de saída
     */
    public static void main(String[] args) {
        String entrada = args.length > 0 ? args[0] : "UEDT_SelectionSort.txt";
        String saida = args.length > 1 ? args[1] : "alunos_ordenados.txt";

        try {
            Aluno[] alunos = lerArquivo(entrada);
            System.out.println(alunos.length + " registros lidos.");

            selectionSort(alunos);

            salvarArquivo(saida, alunos);
            System.out.println("Arquivo ordenado salvo em: " + saida);
        } catch (IOException e) {
            System.out.println("Erro ao acessar arquivo: " + e.getMessage());
        }
    }

    /**
     * Lê um arquivo UTF-8 e converte suas linhas válidas em objetos Aluno.
     *
     * @param caminho caminho do arquivo de entrada
     * @return vetor contendo os alunos lidos do arquivo
     * @throws IOException se ocorrer um erro ao ler o arquivo
     */
    public static Aluno[] lerArquivo(String caminho) throws IOException {
        List<Aluno> lista = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(caminho), StandardCharsets.UTF_8)) {
            String linha;
            int numeroLinha = 0;

            while ((linha = br.readLine()) != null) {
                numeroLinha++;
                linha = linha.trim();
                if (linha.isEmpty()) {
                    continue; // ignora linhas em branco
                }

                // o primeiro campo, antes do primeiro "|", é a matrícula
                String[] campos = linha.split("\\|");
                try {
                    long matricula = Long.parseLong(campos[0].trim());
                    lista.add(new Aluno(matricula, linha));
                } catch (NumberFormatException e) {
                    System.out.println("Linha " + numeroLinha + " ignorada (matrícula inválida): " + linha);
                }
            }
        }

        return lista.toArray(new Aluno[0]);
    }

    /**
     * Ordena o vetor de alunos pela matrícula usando Selection Sort.
     *
     * @param v vetor de alunos a ser ordenado
     */
    public static void selectionSort(Aluno[] v) {
        for (int i = 0; i < v.length - 1; i++) {
            int menor = i;
            for (int j = i + 1; j < v.length; j++) {
                if (v[j].getMatricula() < v[menor].getMatricula()) {
                    menor = j;
                }
            }
            if (menor != i) {
                Aluno temp = v[i];
                v[i] = v[menor];
                v[menor] = temp;
            }
        }
    }

    /**
     * Grava os alunos em um arquivo UTF-8, preservando suas linhas originais.
     *
     * @param caminho caminho do arquivo de saída
     * @param alunos vetor de alunos já ordenado
     * @throws IOException se ocorrer um erro ao gravar o arquivo
     */
    public static void salvarArquivo(String caminho, Aluno[] alunos) throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(caminho), StandardCharsets.UTF_8)) {
            for (Aluno a : alunos) {
                bw.write(a.getLinhaOriginal());
                bw.newLine();
            }
        }
    }
}
