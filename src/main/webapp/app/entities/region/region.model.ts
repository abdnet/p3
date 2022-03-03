import { IDialecte } from 'app/entities/dialecte/dialecte.model';

export interface IRegion {
  id?: number;
  reginId?: number | null;
  regionNom?: string | null;
  pays?: string | null;
  dialectes?: IDialecte[] | null;
}

export class Region implements IRegion {
  constructor(
    public id?: number,
    public reginId?: number | null,
    public regionNom?: string | null,
    public pays?: string | null,
    public dialectes?: IDialecte[] | null
  ) {}
}

export function getRegionIdentifier(region: IRegion): number | undefined {
  return region.id;
}
