import { ILangue } from 'app/entities/langue/langue.model';
import { IAmawalWord } from 'app/entities/amawal-word/amawal-word.model';

export interface ITamazightToLang {
  id?: number;
  traductionId?: number | null;
  langue?: ILangue | null;
  words?: IAmawalWord[] | null;
  amawalWord?: IAmawalWord | null;
}

export class TamazightToLang implements ITamazightToLang {
  constructor(
    public id?: number,
    public traductionId?: number | null,
    public langue?: ILangue | null,
    public words?: IAmawalWord[] | null,
    public amawalWord?: IAmawalWord | null
  ) {}
}

export function getTamazightToLangIdentifier(tamazightToLang: ITamazightToLang): number | undefined {
  return tamazightToLang.id;
}
