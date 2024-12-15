package ufrrj.web2.sis_academico.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "matriculas_disciplinas")
public class MatriculaDisciplina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne(optional = false)
    @JoinColumn(name = "disciplina_ofertada_id", nullable = false)
    private DisciplinaOfertada disciplinaOfertada;

    @Column(nullable = true)
    private Double nota1;

    @Column(nullable = true)
    private Double nota2;

    @Column(name = "data_matricula", nullable = false)
    private LocalDateTime dataMatricula;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public DisciplinaOfertada getDisciplinaOfertada() {
        return disciplinaOfertada;
    }

    public void setDisciplinaOfertada(DisciplinaOfertada disciplinaOfertada) {
        this.disciplinaOfertada = disciplinaOfertada;
    }

    public Double getNota1() {
        return nota1;
    }

    public void setNota1(Double nota1) {
        this.nota1 = nota1;
    }

    public Double getNota2() {
        return nota2;
    }

    public void setNota2(Double nota2) {
        this.nota2 = nota2;
    }

    public LocalDateTime getDataMatricula() {
        return dataMatricula;
    }

    public void setDataMatricula(LocalDateTime dataMatricula) {
        this.dataMatricula = dataMatricula;
    }
}