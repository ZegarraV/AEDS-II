import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Representa um produto perecível (ex.: gêneros alimentícios), que possui
 * data de validade. Não pode ser cadastrado com data de validade anterior
 * ao dia atual, não pode ter seu valor de venda solicitado após o vencimento,
 * e recebe um desconto no preço de venda quando estiver a poucos dias de vencer.
 */
public class ProdutoPerecivel extends Produto {

	private static final double DESCONTO = 0.25;
	private static final int PRAZO_DESCONTO = 7;
	private LocalDate dataDeValidade;

	/**
     * Construtor completo.
     * @param desc Descrição do produto (mínimo de 3 caracteres)
     * @param precoCusto Preço do produto (mínimo 0.01)
     * @param margemLucro Margem de lucro (mínimo 0.01)
     * @param validade Data de validade do produto (não pode ser anterior ao dia atual)
     */
	public ProdutoPerecivel(String desc, double precoCusto, double margemLucro, LocalDate validade) {
		super(desc, precoCusto, margemLucro);

		if (validade == null || validade.isBefore(LocalDate.now())) {
			throw new IllegalArgumentException("Data de validade inválida: não pode ser anterior ao dia atual.");
		}
		this.dataDeValidade = validade;
	}

	/**
     * Retorna o valor de venda do produto, considerando seu preço de custo e margem de lucro.
     * Caso o produto esteja com prazo de validade de 7 dias ou menos, é aplicado um desconto de 25%.
     * @return Valor de venda do produto (double, positivo)
     * @throws IllegalStateException caso o produto esteja fora da data de validade
     */
	@Override
	public double valorVenda() {

		LocalDate hoje = LocalDate.now();

		if (hoje.isAfter(dataDeValidade)) {
			throw new IllegalStateException("Produto fora da data de validade, venda não permitida.");
		}

		double valor = precoCusto * (1.0 + margemLucro);

		long diasParaVencer = ChronoUnit.DAYS.between(hoje, dataDeValidade);
		if (diasParaVencer <= PRAZO_DESCONTO) {
			valor *= (1.0 - DESCONTO);
		}

		return valor;
	}

	/**
     * Descrição, em string, do produto, contendo sua descrição, o valor de venda
     * e a data de validade.
     * @return String com o formato:
     * [NOME]: R$ [VALOR DE VENDA] (Validade: [DATA])
     */
	@Override
	public String toString() {
		return super.toString() + " (Validade: " + dataDeValidade + ")";
	}
}
