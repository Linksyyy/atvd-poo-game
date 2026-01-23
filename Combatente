public abstract class Combatente {
	protected String nome;
	protected int pv;
	protected int pvMax;

	public Combatente(String nome, int pv, int pvMax) {
		this.nome = nome;
		this.pv = pv;
		this.pvMax = pvMax;
	}

	public String getNome() {
		return nome;
	}

	public int getPv() {
		return pv;
	}

	public int getPvMax() {
		return pvMax;
	}

	public boolean vivo() {
		return this.pv > 0;
	}

	public void receberDano(int dano) {
		this.pv = pv - dano;
		if (this.pv < 0) {
			this.pv = 0;
		}
	}

	public abstract int atacar();

}
