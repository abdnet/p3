export interface ILangue {
  id?: number;
  langueId?: number | null;
  langue?: string | null;
}

export class Langue implements ILangue {
  constructor(public id?: number, public langueId?: number | null, public langue?: string | null) {}
}

export function getLangueIdentifier(langue: ILangue): number | undefined {
  return langue.id;
}
