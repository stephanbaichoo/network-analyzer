import {IPortDTO, PortDTO} from "./portDTO.model";

export interface IPortStatistics {
  portDTO?: IPortDTO;

  bytesSum?: Number;
}

export class PortStatistics implements IPortStatistics {
  constructor(
    portDTO: PortDTO,
    bytesSum: Number
  ) {
  }

}
