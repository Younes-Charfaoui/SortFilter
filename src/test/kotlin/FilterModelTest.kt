import com.youtube.Book
import com.youtube.BookOthers
import com.youtube.BookTransforms
import com.youtube.Genre
import com.youtube.transformation.SortType
import com.youtube.transformation.Transformer
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.containsExactly
import strikt.assertions.map
import java.time.LocalDate

class FilterModelTest {

    @Test
    fun `test filtering by genre comedy`() {
        val models = booksData()
        val transformer = Transformer<Book>()

        val result = transformer.transform(models, listOf(BookTransforms.GenreFilter(Genre.Comedy)))

        expectThat(result).map { it.id }.containsExactly(1, 3)
    }

    @Test
    fun `test filtering by genre comedy, sort by desc`() {
        val models = booksData()
        val transformer = Transformer<Book>()

        val result = transformer.transform(
            models = models,
            filters = listOf(BookTransforms.GenreFilter(Genre.Comedy)),
            sorter = BookTransforms.TitleSort(SortType.DESC)
        )

        expectThat(result).map { it.id }.containsExactly(3, 1)
    }

    @Test
    fun `test filtering by genre business`() {
        val models = booksData()
        val transformer = Transformer<Book>()

        val result = transformer.transform(models, listOf(BookTransforms.GenreFilter(Genre.Business)))

        expectThat(result).map { it.id }.containsExactly(2)
    }

    @Test
    fun `test filtering with genre and is best seller`() {
        val models = booksData()
        val transformer = Transformer<Book>()

        val result = transformer.transform(
            models = models,
            filters = listOf(
                BookTransforms.GenreFilter(Genre.Software),
                BookTransforms.IsBestSellerFilter
            )
        )

        expectThat(result).map { it.id }.containsExactly(4, 6, 7, 9)
    }

    @Test
    fun `test filtering with genre and is best seller, sorting desc`() {
        val transformer = Transformer<Book>()

        val result = transformer.transform(
            models = booksData(),
            filters = listOf(
                BookTransforms.IsBestSellerFilter,
                BookTransforms.GenreFilter(Genre.Software),
                BookTransforms.NonEmptyStatus,
            ),
            sorter = BookTransforms.TitleSort(SortType.DESC)
        )

        expectThat(result).map { it.id }.containsExactly(9, 7, 6, 4)
    }

    @Test
    fun `test filtering with multiple genre`() {
        val transformer = Transformer<Book>()

        val result = transformer.transform(
            models = booksData(),
            filters = listOf(
                BookTransforms.GenreFilter(Genre.Software, Genre.Business, Genre.Personal),
            )
        )

        expectThat(result).map { it.id }.containsExactly(2, 4, 5, 6, 7, 8, 9, 10, 11)
    }

    @Test
    fun `test filtering with status not empty and genre non classified, sort on title desc`() {
        val transformer = Transformer<Book>()

        val result = transformer.transform(
            models = booksData(),
            filters = listOf(
                BookTransforms.GenreFilter(Genre.NonClassified),
                BookTransforms.NonEmptyStatus
            ),
            sorter = BookTransforms.TitleSort(SortType.DESC)
        )

        expectThat(result).map { it.title }.containsExactly("N", "L")
    }

    @Test
    fun `test with custom filter called others`() {
        val transformer = Transformer<Book>()

        val result = transformer.transform(
            models = booksData(),
            filters = listOf(
                BookTransforms.OtherFilter(
                    BookOthers.IsBestSeller,
                    BookOthers.IsComedy,
                    BookOthers.IsPersonal,
                    BookOthers.Before2009
                )
            ),
            sorter = BookTransforms.TitleSort()
        )

        expectThat(result).map { it.title }.containsExactly("A","C", "D", "F", "G", "I", "J", "K", "O")
    }

    private fun booksData() = listOf(
        Book(1, "A", genre = Genre.Comedy),
        Book(2, "B", genre = Genre.Business),
        Book(3, "C", genre = Genre.Comedy),
        Book(4, "D", genre = Genre.Software, isBestSeller = true),
        Book(5, "E", genre = Genre.Software),
        Book(6, "F", genre = Genre.Software, isBestSeller = true),
        Book(7, "G", genre = Genre.Software, isBestSeller = true),
        Book(8, "H", genre = Genre.Software),
        Book(9, "I", genre = Genre.Software, isBestSeller = true),
        Book(10, "J", genre = Genre.Personal, isBestSeller = true),
        Book(11, "K", genre = Genre.Personal),
        Book(12, "L", genre = Genre.NonClassified, status = "Data"),
        Book(13, "M", genre = Genre.NonClassified, status = ""),
        Book(14, "N", genre = Genre.NonClassified, status = "Bought"),
        Book(15, "O", genre = Genre.Manga, publishDate = LocalDate.of(2008, 8, 7)),
        Book(16, "P", genre = Genre.Manga, publishDate = LocalDate.of(2010, 10, 7)),
    )
}