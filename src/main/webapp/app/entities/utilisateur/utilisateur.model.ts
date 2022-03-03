import dayjs from 'dayjs/esm';

export interface IUtilisateur {
  id?: number;
  utilisateurId?: number | null;
  nom?: string | null;
  prenom?: string | null;
  adresseEmail?: string | null;
  avatar?: string | null;
  dateInscription?: dayjs.Dayjs | null;
  validProfil?: boolean | null;
  activateProfil?: boolean | null;
  genre?: string | null;
  password?: string | null;
}

export class Utilisateur implements IUtilisateur {
  constructor(
    public id?: number,
    public utilisateurId?: number | null,
    public nom?: string | null,
    public prenom?: string | null,
    public adresseEmail?: string | null,
    public avatar?: string | null,
    public dateInscription?: dayjs.Dayjs | null,
    public validProfil?: boolean | null,
    public activateProfil?: boolean | null,
    public genre?: string | null,
    public password?: string | null
  ) {
    this.validProfil = this.validProfil ?? false;
    this.activateProfil = this.activateProfil ?? false;
  }
}

export function getUtilisateurIdentifier(utilisateur: IUtilisateur): number | undefined {
  return utilisateur.id;
}
