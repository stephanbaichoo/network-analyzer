import dayjs from 'dayjs/esm';

export interface INetflow {
  id?: number;
  dateFirstSeen?: dayjs.Dayjs | null;
  timeFirstSeen?: string | null;
  duration?: number | null;
  protocol?: string | null;
  srcIp?: string | null;
  dstIp?: string | null;
  flags?: string | null;
  tos?: number | null;
  packetNo?: number | null;
  bytes?: string | null;
  pps?: string | null;
  bps?: string | null;
  bpp?: string | null;
  flows?: string | null;
}

export class Netflow implements INetflow {
  constructor(
    public id?: number,
    public dateFirstSeen?: dayjs.Dayjs | null,
    public timeFirstSeen?: string | null,
    public duration?: number | null,
    public protocol?: string | null,
    public srcIp?: string | null,
    public dstIp?: string | null,
    public flags?: string | null,
    public tos?: number | null,
    public packetNo?: number | null,
    public bytes?: string | null,
    public pps?: string | null,
    public bps?: string | null,
    public bpp?: string | null,
    public flows?: string | null
  ) {}
}

export function getNetflowIdentifier(netflow: INetflow): number | undefined {
  return netflow.id;
}
