package li.raphael.kokus.c4.core

import com.structurizr.export.plantuml.C4PlantUMLExporter
import com.structurizr.view.StaticView
import net.sourceforge.plantuml.FileFormat
import net.sourceforge.plantuml.FileFormatOption
import net.sourceforge.plantuml.SourceStringReader
import java.io.OutputStream

fun interface StructurizrViewRenderer {
    fun render(
        view: StaticView,
        output: OutputStream,
    )
}

class PlantUmlRenderer : StructurizrViewRenderer {
    override fun render(
        view: StaticView,
        output: OutputStream,
    ) {
        val exporter = C4PlantUMLExporter()
        val plantUmlSource = exporter.exportWithIntegratedGraphViz(view)
        SourceStringReader(plantUmlSource).outputImage(output, FileFormatOption(FileFormat.SVG))
    }

    private fun C4PlantUMLExporter.exportWithIntegratedGraphViz(view: StaticView): String {
        // Use the integrated GraphViz Java port instead of relying on an external 'dot' binary.
        // See https://plantuml.com/smetana02
        return export(view).definition.replace("@startuml", "@startuml\n!pragma layout smetana")
    }
}
