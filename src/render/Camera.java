package render;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Camera {
	private Vector3f posicao;
	private Quaternionf rotacao;
	private Matrix4f projecao;
	
	public Camera() {
		posicao = new Vector3f();
		rotacao = new Quaternionf();
		projecao = new Matrix4f();
	}
	
	public Matrix4f getTransformacao() {
		Matrix4f world = new Matrix4f();
		world.rotate(rotacao.conjugate(new Quaternionf()));
		world.translate(posicao.mul(-1, new Vector3f()));
		
		return world;
	}
	
	public void setOrtho(float esquerda, float direita, float cima, float baixo) {
		projecao.setOrtho2D(esquerda, direita, baixo, cima);
	}
	
	public void setPerspective(float fielfOfView, float ratio, float zNear, float zFar) {
		projecao.setPerspective(fielfOfView, ratio, zNear, zFar);
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

	public Matrix4f getProjecao() {
		return projecao;
	}

	public void setProjecao(Matrix4f projecao) {
		this.projecao = projecao;
	}
}
