import java.nio.charset.Charset;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

public class App {

    /** Quantidade máxima de produtos que podem ser armazenados no vetor */
    static final int MAX_NOVOS_PRODUTOS = 10;

    /** Nome do arquivo de dados */
    static String nomeArquivoDados;

    /** Scanner para leitura de dados do teclado */
    static Scanner teclado;

    /** Vetor de produtos cadastrados */
    static Produto[] produtosCadastrados;

    /** Quantidade de produtos cadastrados atualmente no vetor */
    static int quantosProdutos = 0;

    /** Gera um efeito de pausa na CLI */
    static void pausa() {
        System.out.println("Digite enter para continuar...");
        teclado.nextLine();
    }

    /** Cabeçalho principal da CLI do sistema */
    static void cabecalho() {
        System.out.println("AEDs II COMÉRCIO DE COISINHAS");
        System.out.println("=============================");
    }

    /** Imprime o menu principal */
    static int menu() {
        cabecalho();

        System.out.println("1 - Listar todos os produtos");
        System.out.println("2 - Procurar e imprimir os dados de um produto");
        System.out.println("3 - Cadastrar novo produto");
        System.out.println("0 - Sair");

        System.out.print("Digite sua opção: ");

        return Integer.parseInt(teclado.nextLine());
    }

    /**
     * Lê os dados de um arquivo-texto e retorna um vetor de produtos.
     */
    static Produto[] lerProdutos(String nomeArquivoDados) {

        try {

            BufferedReader arquivo =
                    new BufferedReader(new FileReader(nomeArquivoDados));

            int quantidade =
                    Integer.parseInt(arquivo.readLine());

            Produto[] produtos =
                    new Produto[quantidade + MAX_NOVOS_PRODUTOS];

            for (int i = 0; i < quantidade; i++) {

                String linha = arquivo.readLine();

                produtos[i] =
                        Produto.criarDoTexto(linha);
            }

            quantosProdutos = quantidade;

            arquivo.close();

            return produtos;

        } catch (Exception e) {

            quantosProdutos = 0;

            return new Produto[MAX_NOVOS_PRODUTOS];
        }
    }

    /**
     * Localiza um produto pelo nome/descrição.
     */
    static void localizarProdutos() {

        System.out.print("Digite a descrição do produto: ");

        String descricao = teclado.nextLine();

        for (int i = 0; i < quantosProdutos; i++) {

            if (produtosCadastrados[i].descricao
                    .equalsIgnoreCase(descricao)) {

                System.out.println(produtosCadastrados[i]);

                return;
            }
        }

        System.out.println("Produto não encontrado.");
    }

    /**
     * Salva os produtos no arquivo.
     */
    public static void salvarProdutos(String nomeArquivo) {

        try {

            PrintWriter arquivo =
                    new PrintWriter(new FileWriter(nomeArquivo));

            arquivo.println(quantosProdutos);

            for (int i = 0; i < quantosProdutos; i++) {

                arquivo.println(
                        produtosCadastrados[i].gerarDadosTexto()
                );
            }

            arquivo.close();

        } catch (Exception e) {

            System.out.println("Erro ao salvar os produtos.");
        }
    }

    /**
     * Lista todos os produtos cadastrados.
     */
    static void listarTodosOsProdutos() {

        if (quantosProdutos == 0) {

            System.out.println("Nenhum produto cadastrado.");

            return;
        }

        for (int i = 0; i < quantosProdutos; i++) {

            System.out.println(
                    (i + 1) + " - " +
                    produtosCadastrados[i]
            );
        }
    }

    /**
     * Rotina para cadastro de um novo produto.
     */
    static void cadastrarProduto() {

        if (quantosProdutos >= produtosCadastrados.length) {

            System.out.println("Limite de produtos atingido.");

            return;
        }

        System.out.println("1 - Produto não perecível");
        System.out.println("2 - Produto perecível");

        System.out.print("Digite o tipo do produto: ");

        int tipo =
                Integer.parseInt(teclado.nextLine());

        System.out.print("Descrição: ");

        String descricao =
                teclado.nextLine();

        System.out.print("Preço de custo: ");

        double precoCusto =
                Double.parseDouble(teclado.nextLine());

        System.out.print("Margem de lucro: ");

        double margemLucro =
                Double.parseDouble(teclado.nextLine());

        if (tipo == 1) {

            produtosCadastrados[quantosProdutos] =
                    new ProdutoNaoPerecivel(
                            descricao,
                            precoCusto,
                            margemLucro
                    );

            quantosProdutos++;

            System.out.println(
                    "Produto cadastrado com sucesso!"
            );

        } else if (tipo == 2) {

            System.out.print(
                    "Data de validade (dd/MM/yyyy): "
            );

            String data =
                    teclado.nextLine();

            java.time.format.DateTimeFormatter formato =
                    java.time.format.DateTimeFormatter
                            .ofPattern("dd/MM/yyyy");

            java.time.LocalDate validade =
                    java.time.LocalDate.parse(
                            data,
                            formato
                    );

            produtosCadastrados[quantosProdutos] =
                    new ProdutoPerecivel(
                            descricao,
                            precoCusto,
                            margemLucro,
                            validade
                    );

            quantosProdutos++;

            System.out.println(
                    "Produto cadastrado com sucesso!"
            );

        } else {

            System.out.println(
                    "Tipo de produto inválido."
            );
        }
    }

    public static void main(String[] args) {

        teclado =
                new Scanner(
                        System.in,
                        Charset.forName("UTF-8")
                );

        nomeArquivoDados =
                "dadosProdutos.csv";

        produtosCadastrados =
                lerProdutos(nomeArquivoDados);

        int opcao = -1;

        do {

            opcao = menu();

            switch (opcao) {

                case 1:
                    listarTodosOsProdutos();
                    break;

                case 2:
                    localizarProdutos();
                    break;

                case 3:
                    cadastrarProduto();
                    break;

                case 0:
                    break;

                default:
                    System.out.println(
                            "Opção inválida."
                    );
            }

            if (opcao != 0) {
                pausa();
            }

        } while (opcao != 0);

        salvarProdutos(nomeArquivoDados);

        teclado.close();
    }
}