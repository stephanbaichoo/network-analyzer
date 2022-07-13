export interface IHost {
  id?: number;
  hostName?: string | null;
  ipAddress?: string | null;
  asname?: string | null;
  org?: string | null;
}

export class Host implements IHost {
  constructor(
    public id?: number,
    public hostName?: string | null,
    public ipAddress?: string | null,
    public asname?: string | null,
    public org?: string | null
  ) {}
}

export function getHostIdentifier(host: IHost): number | undefined {
  return host.id;
}
