package br.com.reusebairro.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String titulo;

    @Column(length = 500)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Categoria categoria;

    @Column(nullable = false, length = 80)
    private String bairro;

    @Column(nullable = false, length = 200)
    private String endereco;

    @Column(length = 8)
    private String cep;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusItem status = StatusItem.DISPONIVEL;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "anunciante_id", nullable = false)
    private Usuario anunciante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coletor_id")
    private Usuario coletor;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    @Column(name = "coletado_em")
    private LocalDateTime coletadoEm;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    public StatusItem getStatus() { return status; }
    public void setStatus(StatusItem status) { this.status = status; }
    public Usuario getAnunciante() { return anunciante; }
    public void setAnunciante(Usuario anunciante) { this.anunciante = anunciante; }
    public Usuario getColetor() { return coletor; }
    public void setColetor(Usuario coletor) { this.coletor = coletor; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
    public LocalDateTime getColetadoEm() { return coletadoEm; }
    public void setColetadoEm(LocalDateTime coletadoEm) { this.coletadoEm = coletadoEm; }
}
