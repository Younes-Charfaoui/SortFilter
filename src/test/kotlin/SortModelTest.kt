import com.youtube.Book
import com.youtube.BookTransforms
import com.youtube.transformation.SortType
import com.youtube.transformation.Transformer
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.containsExactly
import strikt.assertions.map

class SortModelTest {

    @Test
    fun `test sorting model by title property`() {
        val models = booksData()
        val transformer = Transformer<Book>()

        val result = transformer.transform(models,  sorter = BookTransforms.TitleSort())

        expectThat(result).map { it.id }
            .containsExactly(9, 82, 2, 5)
    }

    @Test
    fun `test sorting model by title property desc`() {
        val models = booksData()
        val transformer = Transformer<Book>()

        val result = transformer.transform(models, sorter = BookTransforms.TitleSort(SortType.DESC))

        expectThat(result).map { it.id }.containsExactly(5, 2, 82, 9)
    }

    @Test
    fun `test sorting based on id asc`() {
        val models = booksData()
        val transformer = Transformer<Book>()

        val result = transformer.transform(models, sorter = BookTransforms.IdSort(SortType.ASC))

        expectThat(result).map { it.id }.containsExactly(2, 5, 9, 82)
    }

    @Test
    fun `test sorting based on id desc`() {
        val models = booksData()
        val transformer = Transformer<Book>()

        val result = transformer.transform(models,  sorter = BookTransforms.IdSort(SortType.DESC))

        expectThat(result).map { it.id }.containsExactly(82, 9, 5, 2)
    }

    @Test
    fun `test sorting based on title then id asc`() {
        val models = booksDataTwoSorts()
        val transformer = Transformer<Book>()

        val result = transformer.transform(models, sorter = BookTransforms.TitleIdSort(SortType.ASC))

        expectThat(result).map { it.id }.containsExactly(9, 19, 182, 82, 98, 22, 50, 145)
    }

    @Test
    fun `test sorting based on title then id desc`() {
        val models = booksDataTwoSorts()
        val transformer = Transformer<Book>()

        val result = transformer.transform(models, sorter = BookTransforms.TitleIdSort(SortType.DESC))

        expectThat(result).map { it.id }.containsExactly(145, 50, 22, 98, 82, 182, 19, 9)
    }

    private fun booksData() = listOf(
        Book(5, "D"),
        Book(82, "B"),
        Book(2, "C"),
        Book(9, "A"),
    )

    private fun booksDataTwoSorts() = listOf(
        Book(145, "D"),
        Book(50, "D"),
        Book(82, "BM"),
        Book(182, "BA"),
        Book(22, "CKL"),
        Book(98, "CA"),
        Book(9, "AA"),
        Book(19, "AB"),
    )
}


