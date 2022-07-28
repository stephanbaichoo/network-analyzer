export interface IMostPortDataSummaryModel {
  hour?: string;
  portName?: string;
  portNumber?: string;
  bytesSum?: Number;
}

export class MostPortDataSummary implements IMostPortDataSummaryModel {
  constructor(
    hour?: string,
    portName?: string,
    portNumber?: string,
    bytesSum?: Number,
  ) {
  }

}
