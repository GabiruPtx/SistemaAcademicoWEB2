package ufrrj.web2.sis_academico.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "periodos")
public class Periodo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;

    @OneToMany(mappedBy = "periodo", cascade = CascadeType.ALL)
    private List<DisciplinaOfertada> disciplinas = new ArrayList<>();

    public Periodo() {

    }

    public Periodo(String nome, LocalDate dataInicio, LocalDate dataFim) {
        this.nome = nome;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public List<DisciplinaOfertada> getDisciplinas() {
        return disciplinas;
    }

    public void setDisciplinas(List<DisciplinaOfertada> disciplinas) {
        this.disciplinas = disciplinas;
    }
}