import { IPortDTO, PortDTO } from './portDTO.model';

export interface IPortStatistics {
  portDTO?: IPortDTO;

  bytesSum?: Number;
}

export interface MostPortDataTable {
  portName?: string;

  portNumber?: string;

  hoverDescription?: string;

  OutgoingBytesSum?: number;

  IngoingBytesSum?: number;

  OutgoingPacketsSum?: number;

  IngoingPacketsSum?: number;
}

export class PortStatistics implements IPortStatistics {
  constructor(portDTO: PortDTO, bytesSum: Number) {}
}
