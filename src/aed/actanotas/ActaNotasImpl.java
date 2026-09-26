package aed.actanotas;

import java.util.Comparator;
import java.util.function.Function;

import es.upm.aedlib.Pair;
import es.upm.aedlib.indexedlist.ArrayIndexedList;
import es.upm.aedlib.indexedlist.IndexedList;

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
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IndexedList<Calificacion> getCalificaciones(Function<Calificacion, Boolean> filter,
			Comparator<Calificacion> cmp) {
		// TODO Auto-generated method stub
		return null;
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
