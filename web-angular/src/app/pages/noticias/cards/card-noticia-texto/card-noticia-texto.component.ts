import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { DomSanitizer } from '@angular/platform-browser';

// shared
import { AdvancedCrudCard } from 'src/app/shared/crud/advanced-crud-card';
import { AdvancedCrudController } from 'src/app/shared/crud/advanced-crud.controller';

// aplicação
import { Noticia } from '../../models/noticia';
import { getNoticiaEditorConfig } from '../../core/noticia-editor-config';

import Quill from 'quill'
import ImageResize from 'quill-image-resize-module'
Quill.register('modules/imageResize', ImageResize)

@Component({
  selector: 'app-card-noticia-texto',
  templateUrl: 'card-noticia-texto.component.html',
  styleUrls: ['./card-noticia-texto.component.scss']
})
export class CardNoticiaTextoComponent extends AdvancedCrudCard<Noticia> {

  /**
   * @description Armazena a configuração do editor de texto
   */
  public editorConfig: any;

  /**
   * @description Flag que controla o estado 'checado' do slide-toggle
   */
  public slideChecked: boolean;

  public quillModules;

  constructor(
    public override crudController: AdvancedCrudController<Noticia>,
    public override formBuilder: FormBuilder,
    public sanitizer: DomSanitizer
  ) {
    super(crudController, formBuilder);

    this.registrarEstilosInline();

    // inicializa as variáveis usadas no layout
    this.editorConfig = getNoticiaEditorConfig();
    this.slideChecked = false;

  }

  public get texto(): string {
    return this.form.get('newsletter')?.value;
  }

  criarForm(): FormGroup {
    return this.formBuilder.group({
      newsletter: [null, Validators.required],
    })
  }

  /**
   * @description Executa no toggleChange do do slide-toggle
   */
  public onSlideChange() {
    this.slideChecked = !this.slideChecked;
  }

  // função usada para que o Quill gere estilos inline (exemplo <p style="text-align: center;"...),
  // e não classes do próprio Quill, como ql-align-left, pois isso só funciona para editores Quill,
  // ao usar o html em outro lugar, como no email, se perde o estilo
  public registrarEstilosInline() {
    var DirectionAttribute = Quill.import('attributors/attribute/direction');
    Quill.register(DirectionAttribute, true);

    var AlignClass = Quill.import('attributors/class/align');
    Quill.register(AlignClass, true);

    var BackgroundClass = Quill.import('attributors/class/background');
    Quill.register(BackgroundClass, true);

    var ColorClass = Quill.import('attributors/class/color');
    Quill.register(ColorClass, true);

    var DirectionClass = Quill.import('attributors/class/direction');
    Quill.register(DirectionClass, true);

    var FontClass = Quill.import('attributors/class/font');
    Quill.register(FontClass, true);

    var SizeClass = Quill.import('attributors/class/size');
    Quill.register(SizeClass, true);

    var AlignStyle = Quill.import('attributors/style/align');
    Quill.register(AlignStyle, true);

    var BackgroundStyle = Quill.import('attributors/style/background');
    Quill.register(BackgroundStyle, true);

    var ColorStyle = Quill.import('attributors/style/color');
    Quill.register(ColorStyle, true);

    var DirectionStyle = Quill.import('attributors/style/direction');
    Quill.register(DirectionStyle, true);

    var FontStyle = Quill.import('attributors/style/font');
    Quill.register(FontStyle, true);

    var SizeStyle = Quill.import('attributors/style/size');
    Quill.register(SizeStyle, true);

    this.registrarEstiloParaIdentacoes();
    this.registrarFormatoImagem();
  }

  // Função usada para que o editor Quill gere estilos de
  // identação(exemplo ao colocar a imagem alinhada na direita) que funcionem para html puro.
  public registrarEstiloParaIdentacoes() {
    class IndentAttributor extends Quill.import('parchment').Attributor.Style {
      multiplier = 2;

      constructor(name: string, style: string, params: any) {
        super(name, style, params);
      }

      add(node, value) {
        return super.add(node, `${value * this.multiplier}rem`);
      }

      value(node) {
        return parseFloat(super.value(node)) / this.multiplier || undefined;
      }
    }
    var IndentStyle = new IndentAttributor("indent", "text-indent", {
      scope: Quill.import('parchment').Scope.BLOCK,
      whitelist: ["1em", "2em", "3em", "4em", "5em", "6em", "7em", "8em", "9em"]
    });
    Quill.register(IndentStyle, true);
  }

  // Registra o formato de imagem para o Quill, sem isso, ao deixar uma imagem alinhada na direita,
  // sempre que recarrega o editor Quill, perde o alinhamento.
  public registrarFormatoImagem() {
    var BaseImageFormat = Quill.import('formats/image');
    const ImageFormatAttributesList = [
      'alt',
      'height',
      'width',
      'style'
    ];

    class ImageFormat extends BaseImageFormat {
      static formats(domNode) {
        return ImageFormatAttributesList.reduce(function (formats, attribute) {
          if (domNode.hasAttribute(attribute)) {
            formats[attribute] = domNode.getAttribute(attribute);
          }
          return formats;
        }, {});
      }
      format(name, value) {
        if (ImageFormatAttributesList.indexOf(name) > -1) {
          if (value) {
            this['domNode'].setAttribute(name, value);
          } else {
            this['domNode'].removeAttribute(name);
          }
        } else {
          super.format(name, value);
        }
      }
    }

    Quill.register(ImageFormat, true);
  }

  public getInitData() {
    return {
      language: 'pt_BR',
      selector: 'textarea',
      plugins: 'code anchor autolink charmap codesample emoticons image link lists media searchreplace table visualblocks wordcount checklist mediaembed casechange export formatpainter pageembed linkchecker a11ychecker tinymcespellchecker permanentpen powerpaste advtable advcode editimage tableofcontents footnotes mergetags autocorrect',
      toolbar: 'undo redo | blocks fontfamily fontsize | bold italic underline strikethrough | link image media table mergetags | addcomment showcomments | spellcheckdialog a11ycheck | align lineheight | checklist numlist bullist indent outdent | emoticons charmap | removeformat',      
      paste_data_images: true,
      images_upload_handler: (blobInfo, progress) => new Promise((resolve, reject) => {
        resolve('data:' + blobInfo.blob().type + ';base64,' + blobInfo.base64());
      }
      )
    }
  }

}
