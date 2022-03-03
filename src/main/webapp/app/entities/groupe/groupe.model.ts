export interface IGroupe {
  id?: number;
  groupeId?: number | null;
  groupeCode?: string | null;
  habilitationLevel?: number | null;
}

export class Groupe implements IGroupe {
  constructor(
    public id?: number,
    public groupeId?: number | null,
    public groupeCode?: string | null,
    public habilitationLevel?: number | null
  ) {}
}

export function getGroupeIdentifier(groupe: IGroupe): number | undefined {
  return groupe.id;
}
