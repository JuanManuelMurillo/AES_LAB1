package aed.actanotas;

import java.util.Comparator;
import java.util.HashMap;
import java.util.function.Function;

import es.upm.aedlib.Pair;
import es.upm.aedlib.indexedlist.ArrayIndexedList;
import es.upm.aedlib.indexedlist.IndexedList;

import java.util.Map;

public class ActaNotasImpl implements ActaNotas{
	String asignatura;
	double notaMinimaAprobado;
	int anyo;
	boolean esConvocatoriaExtraordinaria;
	private IndexedList<Calificacion> calificaciones;

	public ActaNotasImpl(String asignatura, double notaMinimaAprobado, int anyo, boolean esConvocatoriaExtraordinaria) {
		this.asignatura = asignatura;
		this.notaMinimaAprobado = notaMinimaAprobado;
		this.anyo = anyo;
		this.esConvocatoriaExtraordinaria = esConvocatoriaExtraordinaria;
		this.calificaciones = new ArrayIndexedList<Calificacion>();
		}

	@Override
	public String asignatura() {
		return asignatura;
	}

	@Override
	public int anyo() {
		return anyo;
	}

	@Override
	public boolean esConvocatoriaExtraordinaria() {
		return esConvocatoriaExtraordinaria;
	}

	@Override
	public double minNotaAprobado() {
		return notaMinimaAprobado;
	}

	@Override
	public ActaNotas addCalificacion(String nombre, String matricula, String grupo, double nota) {
		Calificacion c = new Calificacion(nombre, matricula, grupo, nota);
		if(c.nombreAlumno == null || c.matricula == null || grupo == null || nota < 0.0 || nota >10.0 ) {
			throw new IllegalArgumentException();
		}
		int posicion = 0;
		while(posicion<calificaciones.size() && calificaciones.get(posicion).matricula.compareTo(c.matricula)<0) {
			posicion++;
		}
		if(posicion<calificaciones.size() && calificaciones.get(posicion).matricula.equals(c.matricula)) {
			throw new IllegalStateException();
		}
		calificaciones.add(posicion, c);
		
		return this;
	}

	@Override
	public Calificacion getCalificacion(String matricula) {
		if(matricula == null) {
			throw new IllegalArgumentException();		
		}
		int i = 0;
		Calificacion c = null;
		while(i < calificaciones.size()) {
			if(calificaciones.get(i).matricula.equals(matricula)) {
				c = calificaciones.get(i);
			}
			
			i++;
		}
		return c;
	}
	private int buscarMatricula(String matricula) {
		int posicion = 0;
		while(posicion < calificaciones.size()) {
			if(calificaciones.get(posicion).matricula.equals(matricula)) {
				return posicion;
			}
			posicion++;
		}
		return -1;
		
	}

	@Override
	public ActaNotas updateCalificacion(Calificacion calificacion) {
		if(calificacion == null) {
			throw new IllegalArgumentException();
		}
		int posicion = buscarMatricula(calificacion.matricula);
		if(posicion == -1) {
			throw new IllegalStateException();
		}
		calificaciones.set(posicion, calificacion);
		return this;
	}

	@Override
	public ActaNotas deleteCalificacion(String matricula) {
		if (matricula == null) {
			throw new IllegalArgumentException();
		}

		int posicion = buscarMatricula(matricula);

		if (posicion == -1) {
			throw new IllegalStateException();
		}

		Calificacion calificacion = calificaciones.get(posicion);
		calificaciones.remove(calificacion);

		return this;
	}

	@Override
	public double notaMedia() {
		if(calificaciones.isEmpty()) {
			throw new IllegalStateException();
		}
		int i = 0;
		double media = 0;
		while(i<calificaciones.size()) {
			media+=calificaciones.get(i).nota;
			i++;
		}
		return media/calificaciones.size();
	}

	@Override
	public IndexedList<Pair<String, Integer>> alumnosPorGrupo() {

		Map<String, Integer> contadorGrupos = new HashMap<>();

		for (Calificacion calificacion : calificaciones) {
			String grupo = calificacion.grupo();

			contadorGrupos.put(
				grupo,
				contadorGrupos.getOrDefault(grupo, 0) + 1
			);
		}

		IndexedList<Pair<String, Integer>> alumnos = new ArrayIndexedList<>();

		for (Map.Entry<String, Integer> entry : contadorGrupos.entrySet()) {
			alumnos.add(alumnos.size(), new Pair<String, Integer>(entry.getKey(), entry.getValue()));
		}

		return alumnos;
	}

	@Override
	public IndexedList<Calificacion>
		getCalificaciones(Function<Calificacion,Boolean> filter,
						Comparator<Calificacion> cmp){

    IndexedList<Calificacion> resultado = new ArrayIndexedList<>();

    for (Calificacion calificacion : calificaciones) {
        if (filter == null || filter.apply(calificacion)) {
            resultado.add(resultado.size(), calificacion);
        }
    }

    if (cmp == null) {
        cmp = (c1, c2) -> c1.matricula().compareTo(c2.matricula());
    }

    bubbleSort(resultado, cmp);

    return resultado;
	}

	private void bubbleSort(IndexedList<Calificacion> lista, Comparator<Calificacion> cmp) {

		for (int i = 0; i < lista.size() - 1; i++) {

			boolean intercambiado = false;

			for (int j = 0; j < lista.size() - 1 - i; j++) {

				if (cmp.compare(lista.get(j), lista.get(j + 1)) > 0) {

					Calificacion temp = lista.get(j);

					lista.set(j, lista.get(j + 1));
					lista.set(j + 1, temp);

					intercambiado = true;
				}
			}

			if (!intercambiado) {
				break;
			}
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		else if (obj instanceof ActaNotasImpl) {
			ActaNotasImpl other = (ActaNotasImpl) obj;
			return asignatura.equals(other.asignatura()) && anyo == other.anyo() && esConvocatoriaExtraordinaria == other.esConvocatoriaExtraordinaria();
		}else return false;
	}

	@Override
	public String toString() {
		String convocatoria;
		if(esConvocatoriaExtraordinaria) {
			convocatoria = "extraordinaria";
		}else {
			convocatoria = "ordinaria";
		}
		return "ACTA -> asignatura: " + asignatura + ", anyo: " + anyo + ", convocatoria: " + convocatoria;
	}

}
