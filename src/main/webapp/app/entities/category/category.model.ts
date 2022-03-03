import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { IGroupe } from 'app/entities/groupe/groupe.model';

export interface ICategory {
  id?: number;
  categoryId?: number | null;
  securityLevel?: number | null;
  utilisateurs?: IUtilisateur | null;
  groupe?: IGroupe | null;
}

export class Category implements ICategory {
  constructor(
    public id?: number,
    public categoryId?: number | null,
    public securityLevel?: number | null,
    public utilisateurs?: IUtilisateur | null,
    public groupe?: IGroupe | null
  ) {}
}

export function getCategoryIdentifier(category: ICategory): number | undefined {
  return category.id;
}
