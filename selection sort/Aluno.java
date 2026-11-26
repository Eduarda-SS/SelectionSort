/**
 * Representa um aluno registrado no arquivo de entrada.
 *
 * @author Eduarda Santana Santos
 * @author Gabriel Lira Figueira
 * @author João Luís Aragão
 */
public class Aluno {
    public long matricula;        // 14 dígitos, não cabe em int
    public String linhaOriginal;  // mantida para gravar exatamente como veio

    /**
     * Cria um aluno a partir da matrícula e da linha original do arquivo.
     *
     * @param matricula matrícula usada como chave de ordenação
     * @param linhaOriginal conteúdo completo da linha no arquivo
     */
    public Aluno(long matricula, String linhaOriginal) {
        this.matricula = matricula;
        this.linhaOriginal = linhaOriginal;
    }

    /**
     * Obtém a matrícula do aluno.
     *
     * @return matrícula do aluno
     */
    public long getMatricula() {
        return matricula;
    }

    /**
     * Obtém a linha original registrada no arquivo.
     *
     * @return texto original da linha do aluno
     */
    public String getLinhaOriginal() {
        return linhaOriginal;
    }
}