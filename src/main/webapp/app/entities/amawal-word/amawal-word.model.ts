import { IWordInfo } from 'app/entities/word-info/word-info.model';
import { ITamazightToLang } from 'app/entities/tamazight-to-lang/tamazight-to-lang.model';
import { IWordType } from 'app/entities/word-type/word-type.model';
import { ITheme } from 'app/entities/theme/theme.model';
import { IDialecte } from 'app/entities/dialecte/dialecte.model';

export interface IAmawalWord {
  id?: number;
  wordId?: number | null;
  orthographeTifinagh?: string | null;
  orthographeLatin?: string | null;
  wordInfo?: IWordInfo | null;
  traductions?: ITamazightToLang[] | null;
  wordType?: IWordType | null;
  theme?: ITheme | null;
  dialectes?: IDialecte[] | null;
  tamazightToLang?: ITamazightToLang | null;
}

export class AmawalWord implements IAmawalWord {
  constructor(
    public id?: number,
    public wordId?: number | null,
    public orthographeTifinagh?: string | null,
    public orthographeLatin?: string | null,
    public wordInfo?: IWordInfo | null,
    public traductions?: ITamazightToLang[] | null,
    public wordType?: IWordType | null,
    public theme?: ITheme | null,
    public dialectes?: IDialecte[] | null,
    public tamazightToLang?: ITamazightToLang | null
  ) {}
}

export function getAmawalWordIdentifier(amawalWord: IAmawalWord): number | undefined {
  return amawalWord.id;
}
