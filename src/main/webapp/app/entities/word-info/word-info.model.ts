import dayjs from 'dayjs/esm';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';

export interface IWordInfo {
  id?: number;
  wordInfoId?: number | null;
  contributionDate?: dayjs.Dayjs | null;
  validationDate?: dayjs.Dayjs | null;
  etat?: number | null;
  contributeur?: IUtilisateur | null;
  validateur?: IUtilisateur | null;
}

export class WordInfo implements IWordInfo {
  constructor(
    public id?: number,
    public wordInfoId?: number | null,
    public contributionDate?: dayjs.Dayjs | null,
    public validationDate?: dayjs.Dayjs | null,
    public etat?: number | null,
    public contributeur?: IUtilisateur | null,
    public validateur?: IUtilisateur | null
  ) {}
}

export function getWordInfoIdentifier(wordInfo: IWordInfo): number | undefined {
  return wordInfo.id;
}
