export interface ITheme {
  id?: number;
  themeId?: number | null;
  theme?: string | null;
}

export class Theme implements ITheme {
  constructor(public id?: number, public themeId?: number | null, public theme?: string | null) {}
}

export function getThemeIdentifier(theme: ITheme): number | undefined {
  return theme.id;
}
