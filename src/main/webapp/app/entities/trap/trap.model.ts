import dayjs from 'dayjs/esm';

export interface ITrap {
  id?: number;
  date?: dayjs.Dayjs | null;
  time?: string | null;
  trap?: string | null;
  values?: string | null;
  trigger?: string | null;
}

export class Trap implements ITrap {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs | null,
    public time?: string | null,
    public trap?: string | null,
    public values?: string | null,
    public trigger?: string | null
  ) {}
}

export function getTrapIdentifier(trap: ITrap): number | undefined {
  return trap.id;
}
