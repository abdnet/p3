export interface IWordType {
  id?: number;
  typeId?: number | null;
  type?: string | null;
}

export class WordType implements IWordType {
  constructor(public id?: number, public typeId?: number | null, public type?: string | null) {}
}

export function getWordTypeIdentifier(wordType: IWordType): number | undefined {
  return wordType.id;
}
