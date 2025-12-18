package org.sebsy;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActeurRepositoryTest {

	private static EntityManagerFactory emf;
	private EntityManager em;

	/**
	 * Extraire tous les acteurs triés dans l'ordre alphabétique des identités
	 */
	@Test
	public void testExtraireActeursTriesParIdentite() {

		TypedQuery<Acteur> query = em.createQuery("SELECT a FROM Acteur a order by a.identite", Acteur.class);
		List<Acteur> acteurs = query.getResultList();

		assertEquals(1137, acteurs.size());
		assertEquals("A.J. Danna", acteurs.get(0).getIdentite());
	}

	/**
	 * Extraire l'actrice appelée Marion Cotillard
	 */
	@Test
	public void testExtraireActeursParIdentite() {
		TypedQuery<Acteur> query = em.createQuery("SELECT a FROM Acteur a where a.identite = 'Marion Cotillard'", Acteur.class);
		List<Acteur> acteurs = query.getResultList();

		assertEquals(1, acteurs.size());
		assertEquals("Marion Cotillard", acteurs.get(0).getIdentite());
	}

	/**
	 * Extraire la liste des acteurs dont l'année de naissance est 1985. Astuce:
	 * fonction year(...)
	 */
	@Test
	public void testExtraireActeursParAnneeNaissance() {
		TypedQuery<Acteur> query = em.createQuery(
			"SELECT a FROM Acteur a WHERE YEAR(a.anniversaire) = 1985", 
			Acteur.class
		);
		List<Acteur> acteurs = query.getResultList();
		assertEquals(10, acteurs.size());
	}

	/**
	 * Extraire la liste des actrices ayant joué le rôle d'Harley Quinn
	 */
	@Test
	public void testExtraireActeursParRole() {
		TypedQuery<Acteur> query = em.createQuery(
			"SELECT DISTINCT r.acteur FROM Role r " +
			"WHERE r.nom = 'Harley Quinn'", 
			Acteur.class
		);
		
		List<Acteur> acteurs = query.getResultList();
		assertEquals(1, acteurs.size());
		assertEquals("Margot Robbie", acteurs.get(0).getIdentite());
	}

	/**
	 * Extraire la liste de tous les acteurs ayant joué dans un film paru en 2015.
	 */
	@Test
	public void testExtraireActeursParFilmParuAnnee() {
		TypedQuery<Acteur> query = em.createQuery(
			"SELECT DISTINCT r.acteur FROM Role r " +
			"JOIN r.film f " +
			"WHERE f.annee = :annee", 
			Acteur.class
		).setParameter("annee", 2015);
		
		List<Acteur> acteurs = query.getResultList();
		assertEquals(119, acteurs.size());
	}

	/**
	 * Extraire la liste de tous les acteurs ayant joué dans un film dont le pays d'origine est France
	 */
	@Test
	public void testExtraireActeursParPays() {
		TypedQuery<Acteur> query = em.createQuery(
			"SELECT DISTINCT r.acteur FROM Role r " +
			"JOIN r.film f " +
			"JOIN f.pays p " +
			"WHERE p.nom = 'France'", 
			Acteur.class
		);
		
		List<Acteur> acteurs = query.getResultList();
		assertEquals(158, acteurs.size());
	}

	/**
	 * Extraire la liste de tous les acteurs ayant joué dans un film paru en 2017 et dont le pays d'origine
	 * est France
	 */
	@Test
	public void testExtraireActeursParListePaysEtAnnee() {
		TypedQuery<Acteur> query = em.createQuery(
			"SELECT DISTINCT r.acteur FROM Role r " +
			"JOIN r.film f " +
			"JOIN f.pays p " +
			"WHERE p.nom = 'France' AND f.annee = 2017", 
			Acteur.class
		);
		
		List<Acteur> acteurs = query.getResultList();
		assertEquals(24, acteurs.size());
	}

	/**
	 * Extraire la liste de tous les acteurs ayant joué dans un film réalisé par
	 * Ridley Scott entre les années 2010 et 2020
	 */
	@Test
	public void testExtraireParRealisateurEntreAnnee() {
		TypedQuery<Acteur> query = em.createQuery(
			"SELECT DISTINCT r.acteur FROM Role r " +
			"JOIN r.film f " +
			"JOIN f.realisateurs real " +
			"WHERE real.identite = 'Ridley Scott' " +
			"AND f.annee BETWEEN 2010 AND 2020", 
			Acteur.class
		);
		
		List<Acteur> acteurs = query.getResultList();
		assertEquals(27, acteurs.size());
	}
	
	/**
	 * Extraire la liste de tous les réalisateurs ayant réalisé un film dans lequel Brad Pitt a joué
	 */
	@Test
	public void testExtraireRealisateursParActeur() {
		TypedQuery<Realisateur> query = em.createQuery(
			"SELECT DISTINCT real FROM Realisateur real " +
			"JOIN real.films f " +
			"JOIN f.roles role " +
			"JOIN role.acteur a " +
			"WHERE a.identite = 'Brad Pitt'", 
			Realisateur.class
		);
		
		List<Realisateur> realisateurs = query.getResultList();
		assertEquals(6, realisateurs.size());
	}
	
	@BeforeEach
	public void ouvertureEm() {
		em = emf.createEntityManager();
	}
	
	@AfterEach
	public void fermetureEm() {
		em.close();
	}

	@BeforeAll
	public static void initDatabase() {
		try {
			// Création de l'EntityManagerFactory avec la configuration du persistence.xml
			emf = Persistence.createEntityManagerFactory("movie_db");
			
			// Création d'un EntityManager pour vérifier que tout est correctement initialisé
			EntityManager em = emf.createEntityManager();
			try {
				// Exécution d'une requête simple pour vérifier la connexion
				em.createQuery("SELECT 1").getResultList();
			} finally {
				em.close();
			}
		} catch (Exception e) {
			throw new RuntimeException("Erreur lors de l'initialisation de la base de données", e);
		}
	}

	@AfterAll
	public static void fermetureRessources() {
		emf.close();
	}
}