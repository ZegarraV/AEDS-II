import java.util.Scanner;

/**
 * Pontifícia Universidade Católica de Minas Gerais
 * Bacharelado em Engenharia de Software - Campus Lourdes
 * Algoritmos e Estruturas de Dados II - Semestre 2/2026
 * Oficina de Revisão e Nivelamento: Recursividade
 *
 * Implementação dos algoritmos recursivos solicitados:
 * 1) Soma de números pares até um limite escolhido pelo usuário.
 * 2) Soma de todos os elementos de um vetor de números double.
 * 3) Contagem de repetições de um número escolhido, em um vetor.
 *
 * Um menu principal permite ao usuário escolher qual algoritmo executar
 * e realiza a leitura dos dados necessários para cada um.
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int opcao;

        do {
            exibirMenu();
            opcao = lerOpcao();

            switch (opcao) {
                case 1:
                    executarSomaPares();
                    break;
                case 2:
                    executarSomaVetorDouble();
                    break;
                case 3:
                    executarContagemRepeticoes();
                    break;
                case 0:
                    System.out.println("Encerrando o programa...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }

            System.out.println();

        } while (opcao != 0);

        scanner.close();
    }

    // ---------------------------------------------------------------
    // Menu
    // ---------------------------------------------------------------

    private static void exibirMenu() {
        System.out.println("=========================================");
        System.out.println(" OFICINA DE RECURSIVIDADE - AEDS II");
        System.out.println("=========================================");
        System.out.println("1 - Somar números pares até um limite");
        System.out.println("2 - Somar elementos de um vetor de double");
        System.out.println("3 - Contar repetições de um número em um vetor");
        System.out.println("0 - Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static int lerOpcao() {
        while (!scanner.hasNextInt()) {
            System.out.print("Entrada inválida. Digite um número inteiro: ");
            scanner.next();
        }
        int opcao = scanner.nextInt();
        scanner.nextLine(); // limpa o buffer
        return opcao;
    }

    // ---------------------------------------------------------------
    // Opção 1: Soma de números pares até um limite (recursivo)
    // ---------------------------------------------------------------

    private static void executarSomaPares() {
        System.out.print("Digite o limite até o qual deseja somar os números pares: ");
        int limite = lerInteiro();

        int resultado = somaPares(limite);
        System.out.println("Soma dos números pares de 0 até " + limite + " = " + resultado);
    }

    /**
     * Soma recursivamente todos os números pares entre 0 e n (inclusive).
     *
     * @param n limite superior escolhido pelo usuário
     * @return soma dos números pares no intervalo [0, n]
     */
    private static int somaPares(int n) {
        if (n < 0) {
            return 0;
        }
        if (n % 2 != 0) {
            return somaPares(n - 1);
        }
        if (n == 0) {
            return 0;
        }
        return n + somaPares(n - 2);
    }

    // ---------------------------------------------------------------
    // Opção 2: Soma de vetor de double (recursivo)
    // ---------------------------------------------------------------

    private static void executarSomaVetorDouble() {
        System.out.print("Quantos elementos terá o vetor de double? ");
        int tamanho = lerInteiroPositivo();

        double[] vetor = new double[tamanho];
        for (int i = 0; i < tamanho; i++) {
            System.out.print("Digite o elemento [" + i + "]: ");
            vetor[i] = lerDouble();
        }

        double soma = somaVetorDouble(vetor, 0);
        System.out.println("Soma dos elementos do vetor = " + soma);
    }

    /**
     * Soma recursivamente os elementos de um vetor de double.
     *
     * @param vetor vetor de valores double
     * @param indice índice atual (inicia em 0)
     * @return soma dos elementos a partir do índice informado
     */
    private static double somaVetorDouble(double[] vetor, int indice) {
        if (indice == vetor.length) {
            return 0.0;
        }
        return vetor[indice] + somaVetorDouble(vetor, indice + 1);
    }

    // ---------------------------------------------------------------
    // Opção 3: Contagem de repetições em vetor (recursivo)
    // ---------------------------------------------------------------

    private static void executarContagemRepeticoes() {
        System.out.print("Quantos elementos terá o vetor? ");
        int tamanho = lerInteiroPositivo();

        int[] vetor = new int[tamanho];
        for (int i = 0; i < tamanho; i++) {
            System.out.print("Digite o elemento [" + i + "]: ");
            vetor[i] = lerInteiro();
        }

        System.out.print("Digite o número que deseja procurar no vetor: ");
        int numeroProcurado = lerInteiro();

        int ocorrencias = contarRepeticoes(vetor, numeroProcurado, 0);
        System.out.println("O número " + numeroProcurado + " aparece " + ocorrencias + " vez(es) no vetor.");
    }

    /**
     * Conta recursivamente quantas vezes um número aparece em um vetor.
     *
     * @param vetor vetor de inteiros
     * @param numero número a ser contado
     * @param indice índice atual (inicia em 0)
     * @return quantidade de ocorrências do número a partir do índice informado
     */
    private static int contarRepeticoes(int[] vetor, int numero, int indice) {
        if (indice == vetor.length) {
            return 0;
        }
        int restante = contarRepeticoes(vetor, numero, indice + 1);
        return (vetor[indice] == numero) ? 1 + restante : restante;
    }

    // ---------------------------------------------------------------
    // Métodos auxiliares de leitura
    // ---------------------------------------------------------------

    private static int lerInteiro() {
        while (!scanner.hasNextInt()) {
            System.out.print("Entrada inválida. Digite um número inteiro: ");
            scanner.next();
        }
        int valor = scanner.nextInt();
        scanner.nextLine();
        return valor;
    }

    private static int lerInteiroPositivo() {
        int valor = lerInteiro();
        while (valor <= 0) {
            System.out.print("O valor deve ser maior que zero. Digite novamente: ");
            valor = lerInteiro();
        }
        return valor;
    }

    private static double lerDouble() {
        while (!scanner.hasNextDouble()) {
            System.out.print("Entrada inválida. Digite um número decimal (use ponto): ");
            scanner.next();
        }
        double valor = scanner.nextDouble();
        scanner.nextLine();
        return valor;
    }
}
