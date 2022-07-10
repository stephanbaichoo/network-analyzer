export interface IPort {
  id?: number;
  port?: number | null;
  isTCP?: string | null;
  isUDP?: string | null;
  isSCTP?: string | null;
  description?: string | null;
  name?: string | null;
}

export class Port implements IPort {
  constructor(
    public id?: number,
    public port?: number | null,
    public isTCP?: string | null,
    public isUDP?: string | null,
    public isSCTP?: string | null,
    public description?: string | null,
    public name?: string | null
  ) {}
}

export function getPortIdentifier(port: IPort): number | undefined {
  return port.id;
}
