import com.mathis.livres.domain.models.Livre
import com.mathis.livres.domain.usecase.LivreDAO
import io.kotest.assertions.throwables.shouldThrowWithMessage
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeSortedWith
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll
import io.mockk.clearAllMocks

class LivresDAOTests : FunSpec({

    var dao = LivreDAO()

    beforeTest {
        dao = LivreDAO()
        clearAllMocks()
    }

    test("Le livre est ajouté en base") {
        val livre = Livre(title = "L'Étranger", author = "Albert Camus")

        dao.addLivre(livre)

        dao.livres.size shouldBe 1
        dao.livres.first() shouldBe livre
    }

    test("doit lever une exception si le titre ou l'auteur est vide") {
        val livreInvalide = Livre(title = "", author = "")

        shouldThrowWithMessage<IllegalArgumentException>("Livre author or title missing") {
            dao.addLivre(livreInvalide)
        }

    }

    test("Propriété : la liste récupérée doit toujours être triée par titre") {
        // Générateur : Une liste de 1 à 50 livres avec des textes non vides
        val livreGen = Arb.string(minSize = 1).filter { it.isNotBlank() }

        checkAll(Arb.list(livreGen, range = 1..50)) { titres ->

            // Ajout de livres avec des titres aléatoires
            titres.forEach { titre ->
                dao.addLivre(Livre(title = titre, author = "Auteur Test"))
            }

            val result = dao.fetchAllLivres()

            // 1. Invariant de taille
            result.size shouldBe titres.size

            // 2. Invariant d'ordre : La liste doit être triée par titre
            result.shouldBeSortedWith(compareBy { it.title })
        }
    }

    test("Propriété : l'ajout d'un livre valide doit toujours augmenter la taille de 1") {
        checkAll(
            Arb.string(minSize = 1).filter { it.isNotBlank() },
            Arb.string(minSize = 1).filter { it.isNotBlank() }
        ) { titre, auteur ->
            val tailleInitiale = dao.fetchAllLivres().size

            dao.addLivre(Livre(titre, auteur))

            dao.fetchAllLivres().size shouldBe tailleInitiale + 1
        }
    }

})