# Intelli-J Plug-in Development highlighter examples using Scala

This example is based on  
[Custom Language Support Tutorial](https://www.jetbrains.org/intellij/sdk/docs/tutorials/custom_language_support_tutorial.html) (2-5).

## Language and File Type

### Define a language

```scala
package com.yuchesc.intellij.simple

import com.intellij.lang.Language
import com.intellij.openapi.diagnostic.Logger

class SimpleLanguage extends Language("Simple") {
  val logger = Logger.getInstance(classOf[SimpleLanguage])
  logger.warn("simple language")
}

object SimpleLanguage {
  val Instance = new SimpleLanguage
}
```

### Define an icon

Download [jar-gray.png](https://raw.githubusercontent.com/JetBrains/intellij-sdk-docs/master/code_samples/simple_language_plugin/src/com/simpleplugin/icons/jar-gray.png)
into src/com/yuchesc/intellij/simple/

```scala
package com.yuchesc.intellij.simple

import javax.swing.Icon

import com.intellij.openapi.util.IconLoader

object SimpleIcons {
  val File: Icon = IconLoader.getIcon("/com/yuchesc/intellij/simple/jar-gray.png")
}
```

### Define a file type

This example defines extension '.smpl' instead of '.simple'

```scala
package com.yuchesc.intellij.simple

import javax.swing.Icon

import com.intellij.openapi.fileTypes.LanguageFileType

class SimpleFileType extends LanguageFileType(SimpleLanguage.Instance) {
  override def getName: String = "Simple file"

  override def getDescription: String = "Simple language file"

  override def getIcon: Icon = SimpleIcons.File

  override def getDefaultExtension: String = "smpl"
}

object SimpleFileType {
  val Instance: SimpleFileType = new SimpleFileType
}
```

### Define a file type factory

```scala
package com.yuchesc.intellij.simple

import com.intellij.openapi.fileTypes.{FileTypeConsumer, FileTypeFactory}

class SimpleFileTypeFactory extends FileTypeFactory {
  override def createFileTypes(fileTypeConsumer: FileTypeConsumer): Unit =
    fileTypeConsumer.consume(SimpleFileType.Instance, "smpl")
}
```

* plugin.xml

```xml
    <extensions defaultExtensionNs="com.intellij">
        <fileTypeFactory implementation="com.yuchesc.intellij.simple.SimpleFileTypeFactory" />
    </extensions>
```

## Grammar and Parser

### Define a token type

Create psi package.

```scala
package com.yuchesc.intellij.simple.psi

import com.intellij.psi.tree.IElementType
import com.yuchesc.intellij.simple.SimpleLanguage

class SimpleTokenType(debugName: String)
  extends IElementType(debugName, SimpleLanguage.Instance) {
  override def toString: String = "SimpleTokenType." + super.toString
}
```

### Define an element type

```scala
package com.yuchesc.intellij.simple.psi

import com.intellij.psi.tree.IElementType
import com.yuchesc.intellij.simple.SimpleLanguage

class SimpleElementType(debugName: String)
  extends IElementType(debugName, SimpleLanguage.Instance) {
}
```

### Define grammar

Create src/com/yuchesc/intellij/simple/Simple.bnf

```clike
{
parserClass="com.yuchesc.intellij.simple.SimpleParser"
extends="com.intellij.extapi.psi.ASTWrapperPsiElement"

psiClassPrefix="Simple"
psiImplClassSuffix="Impl"
psiPackage="com.yuchesc.intellij.simple.psi"
psiImplPackage="com.yuchesc.intellij.simple.psi.impl"

elementTypeHolderClass="com.yuchesc.intellij.simple.psi.SimpleTypes"
elementTypeClass="com.yuchesc.intellij.simple.psi.SimpleElementType"
tokenTypeClass="com.yuchesc.intellij.simple.psi.SimpleTokenType"
}

simpleFile ::= item_*
private item_ ::= (property|COMMENT|CRLF)
property ::= (KEY? SEPARATOR VALUE?) | KEY
```

### Generate a parser

After making the bnf, select the file on the project panel, right-click, and choose `Generate Parser Code`.


## Lexer and Parser Definition

You may need [JFlex](http://jflex.de/).

### Define a lexer

Create src/com/yuchesc/intellij/simple/Simple.flex

```clike
package com.yuchesc.intellij.simple;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.yuchesc.intellij.simple.psi.SimpleTypes;
import com.intellij.psi.TokenType;

%%

%class SimpleLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

CRLF=\R
WHITE_SPACE=[\ \n\t\f]
FIRST_VALUE_CHARACTER=[^ \n\f\\] | "\\"{CRLF} | "\\".
VALUE_CHARACTER=[^\n\f\\] | "\\"{CRLF} | "\\".
END_OF_LINE_COMMENT=("#"|"!")[^\r\n]*
SEPARATOR=[:=]
KEY_CHARACTER=[^:=\ \n\t\f\\] | "\\ "

%state WAITING_VALUE

%%

<YYINITIAL> {END_OF_LINE_COMMENT}                           { yybegin(YYINITIAL); return SimpleTypes.COMMENT; }

<YYINITIAL> {KEY_CHARACTER}+                                { yybegin(YYINITIAL); return SimpleTypes.KEY; }

<YYINITIAL> {SEPARATOR}                                     { yybegin(WAITING_VALUE); return SimpleTypes.SEPARATOR; }

<WAITING_VALUE> {CRLF}({CRLF}|{WHITE_SPACE})+               { yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }

<WAITING_VALUE> {WHITE_SPACE}+                              { yybegin(WAITING_VALUE); return TokenType.WHITE_SPACE; }

<WAITING_VALUE> {FIRST_VALUE_CHARACTER}{VALUE_CHARACTER}*   { yybegin(YYINITIAL); return SimpleTypes.VALUE; }

({CRLF}|{WHITE_SPACE})+                                     { yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }

.                                                           { return TokenType.BAD_CHARACTER; }

```

After making the flex, select the file on the project panel, right-click, and choose `Run JFlex Generator`.


### Define an adapter

Modify gen/com/yuchesc/intellij/simple/SimpleLexerAdapter.java

```diff
- public SimpleLexerAdapter() { super(new SimpleLexer()); }
+ public SimpleLexerAdapter() { super(new SimpleLexer(null)); }
```

### Define a root file

```scala
package com.yuchesc.intellij.simple.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.yuchesc.intellij.simple.{SimpleFileType, SimpleLanguage}

class SimpleFile(fileViewProvider: FileViewProvider)
  extends PsiFileBase(fileViewProvider, SimpleLanguage.Instance) {
  override def getFileType: FileType = SimpleFileType.Instance

  override def toString: String = "Simple File"
}
```

### Define a parser definition

```scala
package com.yuchesc.intellij.simple

import com.intellij.lang.ParserDefinition.SpaceRequirements
import com.intellij.lang.{ASTNode, ParserDefinition, PsiParser}
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.{FileViewProvider, PsiElement, PsiFile}
import com.intellij.psi.tree.{IFileElementType, TokenSet}
import com.yuchesc.intellij.simple.psi.{SimpleFile, SimpleTypes}

class SimpleParserDefinition extends ParserDefinition {
  val File: IFileElementType = new IFileElementType(SimpleLanguage.Instance)
  override def getFileNodeType: IFileElementType = File

  override def createElement(astNode: ASTNode): PsiElement = SimpleTypes.Factory.createElement(astNode)

  override def createFile(fileViewProvider: FileViewProvider): PsiFile = new SimpleFile(fileViewProvider)

  override def getStringLiteralElements: TokenSet = TokenSet.EMPTY

  override def spaceExistanceTypeBetweenTokens(astNode: ASTNode, astNode1: ASTNode): ParserDefinition.SpaceRequirements = SpaceRequirements.MAY

  override def createLexer(project: Project): Lexer = new SimpleLexerAdapter()

  val Comments: TokenSet = TokenSet.create(SimpleTypes.COMMENT)
  override def getCommentTokens: TokenSet = Comments

  override def createParser(project: Project): PsiParser = new SimpleParser
}
```

### Register the parser definition

Write this tag in an extensions tag.

```xml
        <lang.parserDefinition language="Simple" implementationClass="com.yuchesc.intellij.simple.SimpleParserDefinition"/>
```

## Syntax Highlighter and Color Settings Page

### Define a syntax highlighter

```scala
package com.yuchesc.intellij.simple

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.{HighlighterColors, DefaultLanguageHighlighterColors => DefaultColors}
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import com.yuchesc.intellij.simple.psi.SimpleTypes
import com.intellij.openapi.editor.colors.TextAttributesKey.{createTextAttributesKey => createKey}
import com.intellij.psi.TokenType

class SimpleSyntaxHighlighter extends SyntaxHighlighterBase {
  override def getTokenHighlights(iElementType: IElementType): Array[TextAttributesKey] =
    SimpleSyntaxHighlighter.KeysMap.getOrElse(iElementType, SimpleSyntaxHighlighter.EmptyKeys)

  override def getHighlightingLexer: Lexer = new SimpleLexerAdapter
}

object SimpleSyntaxHighlighter {
  val Key: TextAttributesKey = createKey("SIMPLE_KEY", DefaultColors.KEYWORD)
  val Separator: TextAttributesKey = createKey("SIMPLE_SEPARATOR", DefaultColors.OPERATION_SIGN)
  val Value: TextAttributesKey = createKey("SIMPLE_VALUE", DefaultColors.STRING)

  val KeysMap: Map[IElementType, Array[TextAttributesKey]] = Map(
    SimpleTypes.SEPARATOR -> Array(Separator),
    SimpleTypes.KEY -> Array[TextAttributesKey](Key),
    SimpleTypes.VALUE -> Array[TextAttributesKey](Value),
    TokenType.BAD_CHARACTER -> Array[TextAttributesKey](createKey("SIMPLE_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER)),
    SimpleTypes.COMMENT -> Array(createKey("SIMPLE_COMMENT", DefaultColors.LINE_COMMENT))
  )
  val EmptyKeys: Array[TextAttributesKey] = Array.empty[TextAttributesKey]
}
```

### Define a syntax highlighter factory

```scala
package com.yuchesc.intellij.simple

import com.intellij.openapi.fileTypes.{SyntaxHighlighter, SyntaxHighlighterFactory}
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class SimpleSyntaxHighlighterFactory extends SyntaxHighlighterFactory {
  override def getSyntaxHighlighter(project: Project, virtualFile: VirtualFile): SyntaxHighlighter = new SimpleSyntaxHighlighter()
}
```

### Register the syntax highlighter factory

Write this tag in an extensions tag.

```xml
        <lang.syntaxHighlighterFactory language="Simple" implementationClass="com.yuchesc.intellij.simple.SimpleSyntaxHighlighterFactory"/>
```

### Define a color settings page

```scala
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
```

### Register the color settings page

Write this tag in an extensions tag.

```xml
        <colorSettingsPage implementation="com.yuchesc.intellij.simple.SimpleColorSettingsPage"/>
```

That's all.