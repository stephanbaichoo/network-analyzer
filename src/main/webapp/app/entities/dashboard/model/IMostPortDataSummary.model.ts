export interface IMostPortDataSummaryModel {
  hour?: string;
  portName?: string;
  portNumber?: string;
  bytesSum?: Number;
}

export interface Data {
  portName?: string;

  bytesPerHour?: number[];
}

export class MostPortDataSummary implements IMostPortDataSummaryModel {
  constructor(hour?: string, portName?: string, portNumber?: string, bytesSum?: Number) {}
}
