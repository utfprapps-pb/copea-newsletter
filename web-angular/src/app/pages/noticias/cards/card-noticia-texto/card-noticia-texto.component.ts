import { DrawerService } from '../../../admin/drawer.service';
import { Component, ViewChild, OnInit, ElementRef } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { DomSanitizer } from '@angular/platform-browser';

// shared
import { AdvancedCrudCard } from 'src/app/shared/crud/advanced-crud-card';
import { AdvancedCrudController } from 'src/app/shared/crud/advanced-crud.controller';

// aplicação
import { Noticia } from '../../models/noticia';

@Component({
  selector: 'app-card-noticia-texto',
  templateUrl: 'card-noticia-texto.component.html',
  styleUrls: ['./card-noticia-texto.component.scss']
})
export class CardNoticiaTextoComponent extends AdvancedCrudCard<Noticia> implements OnInit {

  @ViewChild('editor', { static: true }) public editorComponent: ElementRef;

  public ngOnInit(): void {
  }

  constructor(
    public override crudController: AdvancedCrudController<Noticia>,
    public override formBuilder: FormBuilder,
    public sanitizer: DomSanitizer,
    public drawerService: DrawerService,
  ) {
    super(crudController, formBuilder);
  }

  public get texto(): string {
    return this.form.get('newsletter')?.value;
  }

  criarForm(): FormGroup {
    return this.formBuilder.group({
      newsletter: [null, Validators.required],
    })
  }

  public getInitData() {
    return {
      language: 'pt_BR',
      selector: 'textarea',
      plugins: 'code anchor autolink charmap codesample emoticons image link lists media searchreplace table visualblocks wordcount checklist mediaembed casechange export formatpainter pageembed linkchecker a11ychecker tinymcespellchecker permanentpen powerpaste advtable advcode editimage tableofcontents footnotes mergetags autocorrect autoresize fullscreen preview',
      toolbar: 'undo redo | blocks fontfamily fontsize | bold italic underline strikethrough | link image media table mergetags | addcomment showcomments | spellcheckdialog a11ycheck | align lineheight | checklist numlist bullist indent outdent | emoticons charmap | removeformat',
      paste_data_images: true,
      images_upload_handler: (blobInfo, progress) => new Promise((resolve, reject) => {
        resolve('data:' + blobInfo.blob().type + ';base64,' + blobInfo.base64());
      },
      ),
      image_advtab: true,
      init_instance_callback: function (editor) {
        editor.on('ExecCommand', function (e) {
          console.log('The ' + e.command + ' command was fired.');
        });
      },
      setup: (editor) => { this.addEventFullscreenStateChanged(editor) }
    }
  }

  public addEventFullscreenStateChanged(editor: any) {
    editor.on('FullscreenStateChanged', (state: any) => {
      this.mostrarMenuConformeEditorEmFullscreen(editor, state.state);
    })
  }

  public mostrarMenuConformeEditorEmFullscreen(editor: any, inFullscreen: boolean) {
    if (inFullscreen)
      this.drawerService.drawer.close();
    else {
      this.drawerService.drawer.open();
      //Usado pois tem um bug quando o toggle é para abrir o menu, onde não chama o evento de open,
      //ao contrário do close, que chama, por isso esse código força a chamada de outro evento, que aí sim, chama o evento do open do menu
      editor.fire('blur');
      editor.focus();
    }
    //Usado pois ao fechar o menu, não atualiza sozinho
    this.drawerService.matDrawerContainer.updateContentMargins();
  }

}

