package com.adrian.monuver.feature.settings.data.manager

import android.content.Context
import androidx.core.net.toUri
import com.adrian.monuver.core.domain.model.Transaction
import com.adrian.monuver.core.domain.util.DateHelper
import com.adrian.monuver.core.domain.util.TransactionChildCategory
import com.adrian.monuver.core.domain.util.TransactionParentCategory
import com.adrian.monuver.core.domain.util.TransactionType
import com.adrian.monuver.core.domain.util.toRupiah
import com.adrian.monuver.feature.settings.R
import com.adrian.monuver.feature.settings.domain.manager.ExportManager
import com.adrian.monuver.feature.settings.domain.model.Report
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Chunk
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.TabSettings
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.io.OutputStream
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal class ExportManagerImpl(
    private val context: Context,
) : ExportManager {

    override suspend fun exportToPdf(
        stringUri: String,
        report: Report
    ) {
        withContext(Dispatchers.IO) {
            try {
                val resolver = context.contentResolver
                resolver.openOutputStream(stringUri.toUri())?.use { outputStream ->
                    createPdfDocument(
                        outputStream = outputStream,
                        report = report
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun createPdfDocument(
        report: Report,
        outputStream: OutputStream
    ) {
        val document = Document(PageSize.A4)
        PdfWriter.getInstance(document, outputStream)
        document.open()

        val startPeriod = DateHelper.formatToReadable(report.startDate)
        val endPeriod = DateHelper.formatToReadable(report.endDate)

        val title = Paragraph(
            report.reportName.uppercase(),
            customFont(size = 16f, style = Font.BOLD)
        ).align(Element.ALIGN_CENTER)

        val username = Paragraph()
            .headerAttributes(
                title = context.getString(R.string.report_username),
                content = report.username
            )

        val generatedAt = Paragraph()
            .headerAttributes(
                title = context.getString(R.string.report_generated_at),
                content = DateHelper.formatToReadable(
                    Clock.System.now()
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                        .date.toString()
                )
            )

        val period = Paragraph()
            .headerAttributes(
                title = context.getString(R.string.report_period),
                content = "$startPeriod - $endPeriod"
            )

        val commonTransactionTable = createCommonTransactionTable(
            transactions = report.commonTransactions,
            income = report.totalIncome,
            expense = report.totalExpense
        )
        val transferTransactionTable = createTransferTransactionTable(report.transferTransactions)

        val footer = Paragraph(
            context.getString(R.string.report_footer),
            customFont(style = Font.BOLD)
        ).footerAttributes()

        document.add(title)
        document.add(Paragraph("\n"))
        document.add(Paragraph("\n"))
        document.add(username)
        document.add(generatedAt)
        document.add(period)
        document.add(Paragraph("\n"))
        document.add(Paragraph("\n"))
        document.add(commonTransactionTable)

        if (report.transferTransactions.isNotEmpty()) {
            document.add(Paragraph("\n"))
            document.add(transferTransactionTable)
        }

        document.add(footer)

        document.close()
        outputStream.close()
    }

    private fun createCommonTransactionTable(
        transactions: List<Transaction>,
        income: Long,
        expense: Long
    ): PdfPTable {
        val table = PdfPTable(6)
        table.widthPercentage = 100f
        table.setWidths(floatArrayOf(1.5f, 1.8f, 1.8f, 1.2f, 1.2f, 1.2f))

        val title = Phrase(
            context.getString(R.string.common_transaction_header),
            customFont(size = 10f, style = Font.BOLD)
        )
        val titleCell = PdfPCell(title)
            .defaultPadding()
            .alignCenterMiddle()
            .background(BaseColor.LIGHT_GRAY)
            .colSpan(6)
        table.addCell(titleCell)

        val headers = listOf(
            context.getString(R.string.common_transaction_date),
            context.getString(R.string.common_transaction_title),
            context.getString(R.string.common_transaction_parent_category),
            context.getString(R.string.common_transaction_child_category),
            context.getString(R.string.common_transaction_account),
            context.getString(R.string.common_transaction_amount)
        )
        headers.forEach { header ->
            val header = Phrase(header, customFont(style = Font.BOLD))
            val headerCell = PdfPCell(header)
                .defaultPadding()
                .alignCenterMiddle()
                .background(BaseColor.LIGHT_GRAY)
            table.addCell(headerCell)
        }

        transactions.forEach { transaction ->
            val categoryString = context
                .getString(transaction.parentCategory.toParentCategoryStringRes())
            val subCategoryString = context
                .getString(transaction.childCategory.toChildCategoryStringRes())
            val backgroundColor = when (transaction.type) {
                TransactionType.INCOME -> BaseColor(200, 230, 201)
                TransactionType.EXPENSE -> BaseColor(255, 205, 210)
                else -> BaseColor.WHITE
            }

            val date = Phrase(DateHelper.formatToReadable(transaction.date), customFont())
            val dateCell = PdfPCell(date)
                .defaultPadding()
                .alignLeftMiddle()
                .background(backgroundColor)

            val title = Phrase(transaction.title, customFont())
            val titleCell = PdfPCell(title)
                .defaultPadding()
                .alignLeftMiddle()
                .background(backgroundColor)

            val category = Phrase(categoryString, customFont())
            val categoryCell = PdfPCell(category)
                .defaultPadding()
                .alignLeftMiddle()
                .background(backgroundColor)

            val subCategory = Phrase(subCategoryString, customFont())
            val subCategoryCell = PdfPCell(subCategory)
                .defaultPadding()
                .alignLeftMiddle()
                .background(backgroundColor)

            val source = Phrase(transaction.sourceName, customFont())
            val sourceCell = PdfPCell(source)
                .defaultPadding()
                .alignCenterMiddle()
                .background(backgroundColor)

            val amount = Phrase(
                transaction.amount.toRupiah(),
                customFont(style = Font.BOLD)
            )
            val amountCell = PdfPCell(amount)
                .defaultPadding()
                .alignRightMiddle()
                .background(backgroundColor)

            table.addCell(dateCell)
            table.addCell(titleCell)
            table.addCell(categoryCell)
            table.addCell(subCategoryCell)
            table.addCell(sourceCell)
            table.addCell(amountCell)
        }

        val totalIncomeText = Phrase(
            context.getString(R.string.common_transaction_total_income),
            customFont(style = Font.BOLD)
        )
        val totalIncomeTextCell = PdfPCell(totalIncomeText)
            .defaultPadding()
            .alignCenterMiddle()
            .background(BaseColor.LIGHT_GRAY)
            .colSpan(5)

        val totalIncome = Phrase(income.toRupiah(), customFont(style = Font.BOLD))
        val totalIncomeCell = PdfPCell(totalIncome)
            .defaultPadding()
            .alignRightMiddle()
            .background(BaseColor(200, 230, 201))

        val totalExpenseText = Phrase(
            context.getString(R.string.common_transaction_total_expense),
            customFont(style = Font.BOLD)
        )
        val totalExpenseTextCell = PdfPCell(totalExpenseText)
            .defaultPadding()
            .alignCenterMiddle()
            .background(BaseColor.LIGHT_GRAY)
            .colSpan(5)

        val totalExpense = Phrase(
            expense.toRupiah(),
            customFont(style = Font.BOLD)
        )
        val totalExpenseCell = PdfPCell(totalExpense)
            .defaultPadding()
            .alignRightMiddle()
            .background(BaseColor(255, 205, 210))

        val totalBalanceText = Phrase(
            context.getString(R.string.common_transaction_total_balance),
            customFont(style = Font.BOLD)
        )
        val totalBalanceTextCell = PdfPCell(totalBalanceText)
            .defaultPadding()
            .alignCenterMiddle()
            .background(BaseColor.LIGHT_GRAY)
            .colSpan(5)

        val totalBalance = Phrase(
            (income - expense).toRupiah(),
            customFont(style = Font.BOLD)
        )
        val totalBalanceCell = PdfPCell(totalBalance)
            .defaultPadding()
            .alignRightMiddle()
            .background(BaseColor.WHITE)

        table.addCell(totalIncomeTextCell)
        table.addCell(totalIncomeCell)
        table.addCell(totalExpenseTextCell)
        table.addCell(totalExpenseCell)
        table.addCell(totalBalanceTextCell)
        table.addCell(totalBalanceCell)

        return table
    }

    private fun createTransferTransactionTable(transactions: List<Transaction>): PdfPTable {
        val table = PdfPTable(5)
        table.widthPercentage = 100f

        val title = Phrase(
            context.getString(R.string.transfer_transaction_header),
            customFont(size = 10f, style = Font.BOLD)
        )
        val titleCell = PdfPCell(title)
            .defaultPadding()
            .alignCenterMiddle()
            .background(BaseColor.LIGHT_GRAY)
            .colSpan(5)
        table.addCell(titleCell)

        val headers = listOf(
            context.getString(R.string.common_transaction_date),
            context.getString(R.string.common_transaction_parent_category),
            context.getString(R.string.common_transaction_source),
            context.getString(R.string.common_transaction_destination),
            context.getString(R.string.common_transaction_amount)
        )
        headers.forEach { header ->
            val header = Phrase(header, customFont(style = Font.BOLD))
            val headerCell = PdfPCell(header)
                .defaultPadding()
                .alignCenterMiddle()
                .background(BaseColor.LIGHT_GRAY)
            table.addCell(headerCell)
        }

        transactions.forEach { transaction ->
            val categoryString = context.getString(transaction.childCategory.toChildCategoryStringRes())
            val backgroundColor = BaseColor(187, 222, 251)

            val date = Phrase(DateHelper.formatToReadable(transaction.date), customFont())
            val dateCell = PdfPCell(date)
                .defaultPadding()
                .alignLeftMiddle()
                .background(backgroundColor)

            val category = Phrase(categoryString, customFont())
            val categoryCell = PdfPCell(category)
                .defaultPadding()
                .alignLeftMiddle()
                .background(backgroundColor)

            val source = Phrase(transaction.sourceName, customFont())
            val sourceCell = PdfPCell(source)
                .defaultPadding()
                .alignCenterMiddle()
                .background(backgroundColor)

            val destination = Phrase(transaction.destinationName, customFont())
            val destinationCell = PdfPCell(destination)
                .defaultPadding()
                .alignCenterMiddle()
                .background(backgroundColor)

            val amount = Phrase(
                transaction.amount.toRupiah(),
                customFont(style = Font.BOLD)
            )
            val amountCell = PdfPCell(amount)
                .defaultPadding()
                .alignRightMiddle()
                .background(backgroundColor)

            table.addCell(dateCell)
            table.addCell(categoryCell)
            table.addCell(sourceCell)
            table.addCell(destinationCell)
            table.addCell(amountCell)
        }

        return table
    }

    private fun Int.toParentCategoryStringRes(): Int {
        return when (this) {
            TransactionParentCategory.SALARY -> R.string.salary
            TransactionParentCategory.BONUSES -> R.string.bonuses
            TransactionParentCategory.COMMISSION -> R.string.commission
            TransactionParentCategory.INVESTMENT_RESULT -> R.string.investment_result
            TransactionParentCategory.OTHER_INCOME -> R.string.other_income
            TransactionParentCategory.FOOD_BEVERAGES -> R.string.food_beverages
            TransactionParentCategory.BILLS_UTILITIES -> R.string.bills_utilities
            TransactionParentCategory.TRANSPORTATION -> R.string.transportation
            TransactionParentCategory.HEALTH_PERSONAL_CARE -> R.string.health_personal_care
            TransactionParentCategory.EDUCATION -> R.string.education
            TransactionParentCategory.SHOPPING -> R.string.shopping
            TransactionParentCategory.ENTERTAINMENT -> R.string.entertainment
            TransactionParentCategory.OTHER_EXPENSE -> R.string.other_expense
            TransactionParentCategory.TRANSFER -> R.string.transfer
            else -> 0
        }
    }

    private fun Int.toChildCategoryStringRes(): Int {
        return when (this) {
            TransactionChildCategory.SALARY -> R.string.salary
            TransactionChildCategory.BONUSES -> R.string.bonuses
            TransactionChildCategory.COMMISSION -> R.string.commission
            TransactionChildCategory.INVESTMENT_RESULT -> R.string.investment_result
            TransactionChildCategory.OTHER_INCOME -> R.string.other_income

            TransactionChildCategory.FOOD -> R.string.food
            TransactionChildCategory.DRINK -> R.string.drink
            TransactionChildCategory.TEA_COFFEE -> R.string.tea_coffee
            TransactionChildCategory.GROCERY -> R.string.grocery

            TransactionChildCategory.WATER -> R.string.water
            TransactionChildCategory.ELECTRICITY -> R.string.electricity
            TransactionChildCategory.GAS -> R.string.gas
            TransactionChildCategory.TAX -> R.string.tax
            TransactionChildCategory.HOUSE -> R.string.house
            TransactionChildCategory.INTERNET -> R.string.internet

            TransactionChildCategory.MAINTENANCE -> R.string.maintenance
            TransactionChildCategory.FUEL -> R.string.fuel
            TransactionChildCategory.VEHICLE_ACCESSORIES -> R.string.accessories
            TransactionChildCategory.ONLINE_RIDE -> R.string.online_ride
            TransactionChildCategory.PUBLIC_TRANSPORT -> R.string.public_transport

            TransactionChildCategory.HOSPITAL -> R.string.hospital
            TransactionChildCategory.DOCTOR -> R.string.doctor
            TransactionChildCategory.MEDICINE -> R.string.medicine
            TransactionChildCategory.PERSONAL_CARE -> R.string.personal_care
            TransactionChildCategory.MASSAGE -> R.string.massage
            TransactionChildCategory.SPA -> R.string.spa
            TransactionChildCategory.GYM -> R.string.gym
            TransactionChildCategory.LAUNDRY -> R.string.laundry

            TransactionChildCategory.EDUCATION_FEE -> R.string.education_fee
            TransactionChildCategory.BOOKS_STATIONERY -> R.string.books_stationery
            TransactionChildCategory.COURSE -> R.string.course
            TransactionChildCategory.PRINT_COPY -> R.string.print_copy

            TransactionChildCategory.CLOTHING -> R.string.clothing
            TransactionChildCategory.SHOES -> R.string.shoes
            TransactionChildCategory.BAG -> R.string.bag
            TransactionChildCategory.ACCESSORIES -> R.string.accessories
            TransactionChildCategory.ELECTRONICS -> R.string.electronics
            TransactionChildCategory.FURNITURE -> R.string.furniture
            TransactionChildCategory.VEHICLE -> R.string.vehicle

            TransactionChildCategory.DIGITAL_SUBSCRIPTION -> R.string.digital_subscription
            TransactionChildCategory.CINEMA -> R.string.cinema
            TransactionChildCategory.GAMES -> R.string.games
            TransactionChildCategory.CONCERT_FESTIVAL -> R.string.concert_festival
            TransactionChildCategory.BOOKS_COMICS -> R.string.books_comics
            TransactionChildCategory.HOBBIES_COLLECTIONS -> R.string.hobbies_collections
            TransactionChildCategory.COMMUNITY -> R.string.community

            TransactionChildCategory.DONATION -> R.string.donation
            TransactionChildCategory.INSURANCE -> R.string.insurance
            TransactionChildCategory.INVESTMENT -> R.string.investment
            TransactionChildCategory.OTHER_EXPENSE -> R.string.other_expense
            TransactionChildCategory.SAVINGS_COMPLETE -> R.string.savings_complete

            TransactionChildCategory.TRANSFER_ACCOUNT -> R.string.transfer_account
            TransactionChildCategory.SAVINGS_IN -> R.string.savings_in
            TransactionChildCategory.SAVINGS_OUT -> R.string.savings_out

            else -> 0
        }
    }

    private fun Paragraph.align(alignment: Int): Paragraph {
        this.alignment = alignment
        return this
    }

    private fun Paragraph.headerAttributes(
        title: String,
        content: String
    ): Paragraph {
        this.font = Font(Font.FontFamily.HELVETICA, 10f)
        this.tabSettings = TabSettings(120f)
        this.add(Chunk(title))
        this.add(Chunk.TABBING)
        this.add(Chunk(": $content"))
        return this
    }

    private fun Paragraph.footerAttributes(): Paragraph {
        this.alignment = Element.ALIGN_RIGHT
        this.spacingBefore = 20f
        return this
    }

    private fun customFont(
        family: Font.FontFamily = Font.FontFamily.HELVETICA,
        size: Float = 8f,
        style: Int = Font.NORMAL,
        color: BaseColor = BaseColor.BLACK
    ): Font {
        return Font(family, size, style, color)
    }

    private fun PdfPCell.defaultPadding(): PdfPCell {
        this.paddingLeft = 4f
        this.paddingRight = 4f
        this.paddingTop = 4f
        this.paddingBottom = 6f
        return this
    }

    private fun PdfPCell.alignCenterMiddle(): PdfPCell {
        this.horizontalAlignment = Element.ALIGN_CENTER
        this.verticalAlignment = Element.ALIGN_MIDDLE
        return this
    }

    private fun PdfPCell.alignLeftMiddle(): PdfPCell {
        this.horizontalAlignment = Element.ALIGN_LEFT
        this.verticalAlignment = Element.ALIGN_MIDDLE
        return this
    }

    private fun PdfPCell.alignRightMiddle(): PdfPCell {
        this.horizontalAlignment = Element.ALIGN_RIGHT
        this.verticalAlignment = Element.ALIGN_MIDDLE
        return this
    }

    private fun PdfPCell.background(color: BaseColor): PdfPCell {
        this.backgroundColor = color
        return this
    }

    private fun PdfPCell.colSpan(count: Int): PdfPCell {
        this.colspan = count
        return this
    }
}