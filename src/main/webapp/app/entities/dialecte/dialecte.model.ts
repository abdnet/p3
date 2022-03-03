import { IRegion } from 'app/entities/region/region.model';
import { IAmawalWord } from 'app/entities/amawal-word/amawal-word.model';

export interface IDialecte {
  id?: number;
  dialecteId?: number | null;
  dialecte?: string | null;
  regions?: IRegion[] | null;
  words?: IAmawalWord[] | null;
}

export class Dialecte implements IDialecte {
  constructor(
    public id?: number,
    public dialecteId?: number | null,
    public dialecte?: string | null,
    public regions?: IRegion[] | null,
    public words?: IAmawalWord[] | null
  ) {}
}

export function getDialecteIdentifier(dialecte: IDialecte): number | undefined {
  return dialecte.id;
}
