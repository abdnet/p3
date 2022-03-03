import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'groupe',
        data: { pageTitle: 'amawalAmazighApp.groupe.home.title' },
        loadChildren: () => import('./groupe/groupe.module').then(m => m.GroupeModule),
      },
      {
        path: 'utilisateur',
        data: { pageTitle: 'amawalAmazighApp.utilisateur.home.title' },
        loadChildren: () => import('./utilisateur/utilisateur.module').then(m => m.UtilisateurModule),
      },
      {
        path: 'category',
        data: { pageTitle: 'amawalAmazighApp.category.home.title' },
        loadChildren: () => import('./category/category.module').then(m => m.CategoryModule),
      },
      {
        path: 'word-type',
        data: { pageTitle: 'amawalAmazighApp.wordType.home.title' },
        loadChildren: () => import('./word-type/word-type.module').then(m => m.WordTypeModule),
      },
      {
        path: 'word-info',
        data: { pageTitle: 'amawalAmazighApp.wordInfo.home.title' },
        loadChildren: () => import('./word-info/word-info.module').then(m => m.WordInfoModule),
      },
      {
        path: 'region',
        data: { pageTitle: 'amawalAmazighApp.region.home.title' },
        loadChildren: () => import('./region/region.module').then(m => m.RegionModule),
      },
      {
        path: 'theme',
        data: { pageTitle: 'amawalAmazighApp.theme.home.title' },
        loadChildren: () => import('./theme/theme.module').then(m => m.ThemeModule),
      },
      {
        path: 'langue',
        data: { pageTitle: 'amawalAmazighApp.langue.home.title' },
        loadChildren: () => import('./langue/langue.module').then(m => m.LangueModule),
      },
      {
        path: 'dialecte',
        data: { pageTitle: 'amawalAmazighApp.dialecte.home.title' },
        loadChildren: () => import('./dialecte/dialecte.module').then(m => m.DialecteModule),
      },
      {
        path: 'tamazight-to-lang',
        data: { pageTitle: 'amawalAmazighApp.tamazightToLang.home.title' },
        loadChildren: () => import('./tamazight-to-lang/tamazight-to-lang.module').then(m => m.TamazightToLangModule),
      },
      {
        path: 'amawal-word',
        data: { pageTitle: 'amawalAmazighApp.amawalWord.home.title' },
        loadChildren: () => import('./amawal-word/amawal-word.module').then(m => m.AmawalWordModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
