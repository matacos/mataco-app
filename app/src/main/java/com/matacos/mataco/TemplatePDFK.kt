package com.matacos.mataco

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files

class TemplatePDFK : FileProvider() {
    private var pdfFile: File? = null
    private var document: Document? = null
    private var pdfWriter: PdfWriter? = null
    private var paragraph: Paragraph? = null
    private val fTitle = Font(Font.FontFamily.TIMES_ROMAN, 20f, Font.BOLD)
    private val fSubtitle = Font(Font.FontFamily.TIMES_ROMAN, 18f, Font.BOLD)
    private val fText = Font(Font.FontFamily.TIMES_ROMAN, 12f, Font.BOLD)
    private val fHighText = Font(Font.FontFamily.TIMES_ROMAN, 15f, Font.BOLD)

    fun openDocument() {
        createFile()
        try {

            document = Document(PageSize.A4)
            pdfWriter = PdfWriter.getInstance(document!!, FileOutputStream(pdfFile!!))
            document!!.open()

        } catch (e: Exception) {
            Log.e("openDocument", e.toString())
        }

    }

    private fun createFile() {
        val folder = File(Environment.getExternalStorageDirectory().toString(), "Download")
        if (!folder.exists()) {
            folder.mkdirs()
        }
        pdfFile = File(folder, "certificado_de_alumno_regular.pdf")
    }

    fun addMetaData(title: String, subject: String, author: String) {
        document!!.addTitle(title)
        document!!.addSubject(subject)
        document!!.addAuthor(author)
    }

    fun addTitles(title: String, subtitle: String, date: String) {
        paragraph = Paragraph()
        paragraph!!.spacingBefore = 30f
        addChildP(Paragraph(title, fTitle))
        addChildP(Paragraph(subtitle, fSubtitle))
        addChildP(Paragraph("Fecha de expedición : $date", fHighText))
        paragraph!!.spacingAfter = 30f
        try {
            document!!.add(paragraph)
        } catch (e: DocumentException) {
            e.printStackTrace()
        }

    }

    private fun addChildP(childParagraph: Paragraph) {
        childParagraph.alignment = Element.ALIGN_CENTER
        paragraph!!.add(childParagraph)
    }

    fun addParagraph(text: String) {
        paragraph = Paragraph(text, fText)
        paragraph!!.spacingAfter = 10f
        paragraph!!.spacingBefore = 10f
        paragraph!!.firstLineIndent = 10f
        try {
            document!!.add(paragraph)
        } catch (e: DocumentException) {
            e.printStackTrace()
        }

    }

    fun closeDocument() {
        document!!.close()
    }

    fun viewPDF(context: Context) {
        if (pdfFile!!.exists()) {
            //Uri uri = Uri.fromFile(pdfFile);
            val uri = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".my.package.name.provider", pdfFile!!)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(uri, "application/pdf")
            try {
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("marrket://details?id=com.adobe.reader")))
                Toast.makeText(context, "No hay aplicación para ver el PDF", Toast.LENGTH_SHORT).show()
            }

        } else {
            Toast.makeText(context, "No existe el PDF", Toast.LENGTH_SHORT).show()
        }
    }

    fun addImage(image: Image) {

        try {
            image.alignment = Element.ALIGN_CENTER
            image.scalePercent(25f)
            document!!.add(image)
        } catch (e: DocumentException) {
            e.printStackTrace()
        }

    }
}