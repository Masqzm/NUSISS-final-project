import { Component } from '@angular/core';
import { SvgIcons } from './svg.icons';

@Component({
  selector: 'app-footer',
  standalone: false,
  templateUrl: './footer.component.html',
  styleUrl: './footer.component.css'
})
export class FooterComponent {
  icons = SvgIcons
}
