package render;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Transform {
	private Vector3f posicao;
	private Quaternionf rotacao;
	private Vector3f escala;
	
	public Transform() {
		posicao = new Vector3f();
		rotacao = new Quaternionf();
		escala = new Vector3f(1);
	}
	
	public Matrix4f getTransformacao() {
		Matrix4f matrix = new Matrix4f();
		matrix.translate(posicao);
		matrix.rotate(rotacao);
		matrix.scale(escala);
		return matrix;
	}

	public Vector3f getPosicao() {
		return posicao;
	}

	public void setPosicao(Vector3f posicao) {
		this.posicao = posicao;
	}

	public Quaternionf getRotacao() {
		return rotacao;
	}

	public void setRotacao(Quaternionf rotacao) {
		this.rotacao = rotacao;
	}

	public Vector3f getEscala() {
		return escala;
	}

	public void setEscala(Vector3f escala) {
		this.escala = escala;
	}
	
}
