package com.yuchesc.intellij.simple

import java.util
import javax.swing.Icon

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.{AttributesDescriptor, ColorDescriptor, ColorSettingsPage}
import com.yuchesc.intellij.simple.psi.SimpleTypes

class SimpleColorSettingsPage extends ColorSettingsPage {
  override def getHighlighter: SyntaxHighlighter = new SimpleSyntaxHighlighter

  override def getAdditionalHighlightingTagToDescriptorMap: util.Map[String, TextAttributesKey] = null

  override def getIcon: Icon = SimpleIcons.File

  override def getDemoText: String = "# You are reading the \".properties\" entry.\n" +
    "! The exclamation mark can also mark text as comments.\n" +
    "website = http://en.wikipedia.org/\n" +
    "language = English\n" +
    "# The backslash below tells the application to continue reading\n" +
    "# the value onto the next line.\n" +
    "message = Welcome to \\\n" +
    "          Wikipedia!\n" +
    "# Add spaces to the key\n" +
    "key\\ with\\ spaces = This is the value that could be looked up with the key \"key with spaces\".\n" +
    "# Unicode\n" +
    "tab : \\u0009"

  override def getAttributeDescriptors: Array[AttributesDescriptor] = SimpleColorSettingsPage.Descriptors

  override def getDisplayName: String = "Simple Lang"

  override def getColorDescriptors: Array[ColorDescriptor] = ColorDescriptor.EMPTY_ARRAY
}

object SimpleColorSettingsPage {
  val Descriptors = Array(
    new AttributesDescriptor("Key", SimpleSyntaxHighlighter.Key),
    new AttributesDescriptor("Separator", SimpleSyntaxHighlighter.Separator),
    new AttributesDescriptor("Value", SimpleSyntaxHighlighter.Value)
  )
}